/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import io.zold.api.Copies.Copy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;

/**
 * Multiple copies of a Wallet.
 * @since 1.0
 */
public final class Copies extends IterableEnvelope<Copy> {

    /**
     * Ctor.
     * @param id Id of the wallet to pull
     * @param remotes Remote nodes
     */
    Copies(final long id, final Iterable<Remote> remotes) {
        super(new IterableOf<>(() -> copies(id, remotes).iterator()));
    }

    /**
     * Builds copies from remotes.
     * @param id Wallet's id
     * @param remotes List of remotes
     * @return Iterable Iterable of Copy
     * @throws IOException If fails
     */
    private static Iterable<Copy> copies(final long id,
        final Iterable<Remote> remotes) throws IOException {
        final List<Copy> copies = new ArrayList<>(10);
        for (final Remote remote : remotes) {
            final Wallet wallet = remote.pull(id);
            boolean updated = false;
            for (int idx = 0; idx < copies.size(); idx += 1) {
                final Copy copy = copies.get(idx);
                if (Copies.equalWallets(copies.get(idx).wallet(), wallet)) {
                    copies.set(idx, copy.with(remote));
                    updated = true;
                }
            }
            if (!updated) {
                copies.add(new Copies.Copy(wallet, remote));
            }
        }
        return new IterableOf<>(copies.iterator());
    }

    /**
     * Checks if content of two wallets is equal.
     * @param first First wallet
     * @param second Second wallet
     * @return Boolean Boolean
     * @throws IOException If fails
     */
    private static boolean equalWallets(final Wallet first,
        final Wallet second) throws IOException {
        final List<Transaction> head = new ListOf<>(first.ledger());
        final List<Transaction> tail = new ListOf<>(second.ledger());
        boolean equal = first.id() == second.id()
            && first.key().equals(second.key())
            && head.size() == tail.size();
        int idx = 0;
        while (equal && idx < head.size()) {
            equal = Copies.equalTransactions(head.get(idx), tail.get(idx));
            idx += 1;
        }
        return equal;
    }

    /**
     * Checks if two transactions carry the same content across every field
     * exposed by {@link Transaction}.
     * @param first First transaction
     * @param second Second transaction
     * @return True when every field matches
     * @throws IOException If fails
     */
    private static boolean equalTransactions(final Transaction first,
        final Transaction second) throws IOException {
        return Copies.equalNumbers(first, second)
            && Copies.equalText(first, second);
    }

    /**
     * Checks the numeric fields of two transactions.
     * @param first First transaction
     * @param second Second transaction
     * @return True when id, time, and amount match
     * @throws IOException If fails
     */
    private static boolean equalNumbers(final Transaction first,
        final Transaction second) throws IOException {
        return first.id() == second.id()
            && first.amount() == second.amount()
            && first.time().equals(second.time());
    }

    /**
     * Checks the textual fields of two transactions.
     * @param first First transaction
     * @param second Second transaction
     * @return True when prefix, bnf, details, and signature match
     * @throws IOException If fails
     */
    private static boolean equalText(final Transaction first,
        final Transaction second) throws IOException {
        return first.prefix().equals(second.prefix())
            && first.bnf().equals(second.bnf())
            && first.details().equals(second.details())
            && first.signature().equals(second.signature());
    }

    /**
     * One copy of a {@link Wallet}.
     * @since 1.0
     */
    static final class Copy implements Comparable<Copy> {

        /**
         * The wallet.
         */
        private final Wallet wlt;

        /**
         * The remote nodes where the wallet was found.
         */
        private final Iterable<Remote> remotes;

        /**
         * Ctor.
         * @param wallet The wallet
         * @param remotes The remote nodes where the wallet was found
         */
        Copy(final Wallet wallet, final Remote... remotes) {
            this(wallet, new IterableOf<>(remotes));
        }

        /**
         * Ctor.
         * @param wallet The wallet
         * @param remotes The remote nodes where the wallet was found
         */
        Copy(final Wallet wallet, final Iterable<Remote> remotes) {
            this.wlt = wallet;
            this.remotes = remotes;
        }

        @Override
        public int compareTo(final Copy other) {
            return this.score().compareTo(other.score());
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Copy
                && this.compareTo((Copy) obj) == 0;
        }

        @Override
        public int hashCode() {
            return this.wlt.hashCode();
        }

        /**
         * Creates new Copy instance with additional remote.
         * @param remote Remote
         * @return Copy Copy
         */
        Copies.Copy with(final Remote remote) {
            return new Copies.Copy(this.wallet(), new Joined<>(remote, this.remotes));
        }

        /**
         * The wallet.
         * @return The wallet
         */
        Wallet wallet() {
            return this.wlt;
        }

        /**
         * The summary of the score of all the remote nodes.
         * @return The score
         */
        Score score() {
            return new Score.Summed(new Mapped<>(Remote::score, this.remotes));
        }
    }
}
