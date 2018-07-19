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

import java.util.Iterator;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.HeadOf;
import org.cactoos.iterable.LengthOf;

/**
 * Network of remote nodes.
 *
 * @since 0.1
 * @checkstyle MagicNumberCheck (500 lines)
 */
public interface Network extends Iterable<Remote> {
    /**
     * Push the wallet to the network. The network will select the
     * remote node with the highest score (with a minimum of {@code 16}).
     * @param wallet The wallet
     */
    void push(Wallet wallet);

    /**
     * Pull a wallet from the network.
     * @param id The wallet's {@link Wallet#id() id}
     * @return The wallet
     */
    Wallet pull(Long id);

    /**
     * Simple network implementation.
     */
    final class Simple implements Network {

        /**
         * {@link Remote} nodes.
         */
        private final Iterable<Remote> nodes;

        /**
         * Constructor.
         * @param remotes Remotes of the network
         */
        Simple(final Iterable<Remote> remotes) {
            this.nodes =  remotes;
        }

        @Override
        public void push(final Wallet wallet) {
            new HeadOf<Remote>(
                1,
                new Filtered<Remote>(
                    remote -> new LengthOf(
                        remote.score().suffixes()
                    ).intValue() >= 16,
                    this.nodes
                )
            ).forEach(
                remote -> remote.push(wallet)
            );
        }

        // @todo #5:30min Implement pull method. Pulling a wallet from the
        //  network should return all the wallets with that id in the
        //  network merged together. After the implementation
        //  NetworkTest.pullIsNotYetImplemented() have to be uncommented and
        //  test if pull method is behaving correctle.
        @Override
        public Wallet pull(final Long id) {
            throw new UnsupportedOperationException("pull(id) not supported");
        }

        @Override
        public Iterator<Remote> iterator() {
            return this.nodes.iterator();
        }
    }
}
