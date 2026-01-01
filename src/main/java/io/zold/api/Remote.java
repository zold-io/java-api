/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */

package io.zold.api;

import org.cactoos.iterable.Repeated;
import org.cactoos.text.RandomText;

/**
 * Remote node.
 *
 * @since 0.1
 */
public interface Remote {
    /**
     * This remote node's score.
     * @return The score
     */
    Score score();

    /**
     * Pushes a {@link Wallet} to this remote.
     * @param wallet Wallet to be pushed to this remote
     */
    void push(Wallet wallet);

    /**
     * Pull a wallet from this remote.
     * @param id The wallet's {@link Wallet#id() id}
     * @return The wallet
     */
    Wallet pull(long id);

    /**
     * A Fake {@link Remote}.
     */
    final class Fake implements Remote {

        /**
         * The remote's score.
         */
        private final Score score;

        /**
         * Ctor.
         * @param val The remote's score value
         */
        public Fake(final int val) {
            this(new RtScore(
                new Repeated<>(val, new RandomText())
            ));
        }

        /**
         * Ctor.
         * @param score The remote's score
         */
        public Fake(final Score score) {
            this.score = score;
        }

        @Override
        public Score score() {
            return this.score;
        }

        @Override
        public void push(final Wallet wallet) {
            // nothing
        }

        @Override
        public Wallet pull(final long id) {
            return new Wallet.Fake(id);
        }
    }
}
