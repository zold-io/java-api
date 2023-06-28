/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2023 Zerocracy
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

import java.io.IOException;
import java.util.Iterator;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.Reduced;

/**
 * Network implementation.
 *
 * @since 0.1
 * @todo #5:30min We must figure out how to 'load' some network. Loading the
 *  network will be loading a local JSON file that contains data on all
 *  remote nodes that we know about; we must have a pre configured set of
 *  remote nodes built in too. See whitepaper for details.
 */
public final class RtNetwork implements Network {

    /**
     * {@link Remote} nodes.
     */
    private final Iterable<Remote> nodes;

    /**
     * Constructor.
     * @param remotes Remotes of the network
     */
    RtNetwork(final Iterable<Remote> remotes) {
        this.nodes =  remotes;
    }

    @Override
    public void push(final Wallet wallet) {
        this.nodes.forEach(
            remote -> remote.push(wallet)
        );
    }

    @Override
    public Wallet pull(final long id) throws IOException {
        return new IoCheckedScalar<>(
            new Reduced<>(
                Wallet::merge,
                new Mapped<>(
                    c -> c::wallet,
                    new Sorted<>(new Copies(id, this))
                )
            )
        ).value();
    }

    @Override
    public Iterator<Remote> iterator() {
        return this.nodes.iterator();
    }
}
