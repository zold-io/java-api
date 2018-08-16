/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.zold.api;

import io.zold.api.Copies.Copy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.collection.CollectionOf;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;

/**
 * Multiple copies of a Wallet.
 * @since 1.0
 */
public final class Copies extends IterableEnvelope<Copy> {

    /**
     * Ctor.
     * @param id Id of the wallet to pull.
     * @param remotes Remote nodes.
     */
    Copies(final long id, final Iterable<Remote> remotes) {
        super(() -> copies(id, remotes));
    }

    /**
     * Builds copies from remotes.
     * @param id Wallet's id
     * @param remotes List of remotes
     * @return Iterable Iterable of Copy
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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
                copies.add(new Copy(wallet, remote));
            }
        }
        return new IterableOf<>(copies);
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
        return first.id() == second.id() && new CollectionOf<>(
            first.ledger()
        ).size() == new CollectionOf<>(second.ledger()).size();
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
         * @param wallet The wallet.
         * @param remotes The remote nodes where the wallet was found.
         */
        Copy(final Wallet wallet, final Remote... remotes) {
            this(wallet, new IterableOf<>(remotes));
        }

        /**
         * Ctor.
         * @param wallet The wallet.
         * @param remotes The remote nodes where the wallet was found.
         */
        Copy(final Wallet wallet, final Iterable<Remote> remotes) {
            this.wlt = wallet;
            this.remotes = remotes;
        }

        /**
         * Creates new Copy instance with additional remote.
         * @param remote Remote
         * @return Copy Copy
         */
        public Copy with(final Remote remote) {
            return new Copy(this.wallet(), new Joined<>(remote, this.remotes));
        }

        /**
         * The wallet.
         * @return The wallet.
         */
        public Wallet wallet() {
            return this.wlt;
        }

        /**
         * The summary of the score of all the remote nodes.
         * @return The score.
         */
        public Score score() {
            return new Score.Summed(new Mapped<>(Remote::score, this.remotes));
        }

        @Override
        public int compareTo(final Copy other) {
            return this.score().compareTo(other.score());
        }
    }
}
