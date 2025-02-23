/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
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
