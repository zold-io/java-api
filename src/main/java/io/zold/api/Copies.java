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
     * @todo #56:30min Compare the entire content of two wallets. In addition
     *  to id, compare RSA key and all transactions one by one. Entire content
     *  of each transaction should be compared.
     */
    private static boolean equalWallets(final Wallet first,
        final Wallet second) throws IOException {
        return first.id() == second.id() && new ListOf<>(
            first.ledger()
        ).size() == new ListOf<>(second.ledger()).size();
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
