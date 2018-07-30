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
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;

/**
 * Multiple copies of a Wallet.
 *
 * @since 1.0
 * @todo #41:30min The constructor of Copies should be implemented
 *  to retrieve the Wallet with the provided id in the provided remotes and
 *  group them by equivalent wallet (i.e., equivalent content) along
 *  with their remotes as explained in the whitepaper. A unit test should be
 *  added to validate this behaviour. The unit test
 *  NetworkTest.pullIsNotYetImplemented() should also be removed and replaced
 *  with a real test.
 */
public final class Copies extends IterableEnvelope<Copy> {

    /**
     * Ctor.
     *
     * @param id Id of the wallet to pull.
     * @param remotes Remote nodes.
     */
    Copies(final long id, final Iterable<Remote> remotes) {
        super(() -> new IterableOf<>(new Copy(new Wallet.Fake(id), remotes)));
    }

    /**
     * One copy of a {@link Wallet}.
     *
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
         *
         * @param wallet The wallet.
         * @param remotes The remote nodes where the wallet was found.
         */
        Copy(final Wallet wallet, final Iterable<Remote> remotes) {
            this.wlt = wallet;
            this.remotes = remotes;
        }

        /**
         * The wallet.
         *
         * @return The wallet.
         */
        public Wallet wallet() {
            return this.wlt;
        }

        /**
         * The summary of the score of all the remote nodes.
         *
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
