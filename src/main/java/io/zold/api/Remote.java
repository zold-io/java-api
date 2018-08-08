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
            throw new UnsupportedOperationException(
                "push() not yet supported"
            );
        }

        @Override
        public Wallet pull(final long id) {
            throw new UnsupportedOperationException(
                "pull() not yet supported"
            );
        }
    }
}
