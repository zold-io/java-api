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

import java.util.HashMap;
import java.util.Map;
import org.cactoos.iterable.Repeated;
import org.cactoos.scalar.UncheckedScalar;
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
         * Pushed wallets.
         */
        private final Map<Long, Wallet> wallets;

        /**
         * Ctor.
         * @param val The remote's score
         */
        Fake(final int val) {
            this(
                new RtScore(new Repeated<>(val, new RandomText())),
                new HashMap<>()
            );
        }

        /**
         * Ctor.
         * @param score The remote's score
         * @param wallets Wallets pushed
         */
        Fake(final Score score, final Map<Long, Wallet> wallets) {
            this.score = score;
            this.wallets = wallets;
        }

        @Override
        public Score score() {
            return this.score;
        }

        @Override
        public void push(final Wallet wallet) {
            this.wallets.compute(
                new UncheckedScalar<>(() -> wallet.id()).value(),
                (id, stored) -> {
                    final Wallet result;
                    if (stored == null) {
                        result = wallet;
                    } else {
                        result = stored.merge(wallet);
                    }
                    return result;
                }
            );
        }

        @Override
        public Wallet pull(final long id) {
            return this.wallets.get(id);
        }

        /**
         * Retrieve wallets stored in memory. Used for testing.
         * @return Pushed wallets
         */
        public Map<Long, Wallet> wallets() {
            return this.wallets;
        }
    }
}
