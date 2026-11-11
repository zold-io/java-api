/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Function;
import javax.json.JsonObject;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Reduced;

/**
 * Network implementation.
 *
 * @since 0.1
 */
public final class RtNetwork implements Network {

    /**
     * {@link Remote} nodes.
     */
    private final Iterable<Remote> nodes;

    /**
     * New network loaded from a local JSON file.
     *
     * @param file JSON file with remote descriptors
     * @param builtins Pre-configured remote nodes
     * @param loader Converts a JSON descriptor into a remote node
     * @throws IOException If the file cannot be read
     */
    public RtNetwork(
        final Path file,
        final Iterable<Remote> builtins,
        final Function<JsonObject, Remote> loader
    ) throws IOException {
        this(
            new Joined<>(
                new IterableOf<>(
                    builtins, new Loaded(file, loader)
                )
            )
        );
    }

    /**
     * Constructor.
     *
     * @param remotes Remotes of the network
     */
    RtNetwork(final Iterable<Remote> remotes) {
        this.nodes = remotes;
    }

    @Override
    public void push(final Wallet wallet) {
        this.nodes.forEach(
            remote -> remote.push(wallet)
        );
    }

    @Override
    public Wallet pull(final long id) throws IOException {
        return new IoChecked<>(
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
