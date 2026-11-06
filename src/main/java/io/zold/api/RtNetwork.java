/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Reduced;

/**
 * Network implementation.
 * @since 0.1
 */
public final class RtNetwork implements Network {

    /**
     * {@link Remote} nodes.
     */
    private final Iterable<Remote> nodes;

    /**
     * New network loaded from a local JSON file.
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
            new Joined<Remote>(
                new IterableOf<Iterable<Remote>>(
                    builtins, new RtNetwork.Loaded(file, loader)
                )
            )
        );
    }

    /**
     * Constructor.
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

    /**
     * Loads remote descriptors from JSON.
     * @param file JSON file
     * @param loader Descriptor loader
     * @return Loaded remotes
     * @throws IOException If the file cannot be read
     */
    private static Iterable<Remote> load(
        final Path file,
        final Function<JsonObject, Remote> loader
    ) throws IOException {
        final List<Remote> remotes = new ArrayList<>(0);
        try (
            JsonReader reader = Json.createReader(
                Files.newBufferedReader(file, StandardCharsets.UTF_8)
            )
        ) {
            reader.readArray().stream()
                .map(value -> (JsonObject) value)
                .map(loader)
                .forEach(remotes::add);
        }
        return remotes;
    }

    /**
     * Loaded remotes.
     * @since 1.0
     */
    private static final class Loaded implements Iterable<Remote> {

        /**
         * JSON file.
         */
        private final Path file;

        /**
         * Descriptor loader.
         */
        private final Function<JsonObject, Remote> loader;

        /**
         * New loaded remotes.
         * @param file JSON file
         * @param loader Descriptor loader
         * @throws IOException If the file cannot be read
         */
        Loaded(
            final Path file,
            final Function<JsonObject, Remote> loader
        ) throws IOException {
            this.file = file;
            this.loader = loader;
        }

        @Override
        public Iterator<Remote> iterator() {
            try {
                return RtNetwork.load(this.file, this.loader).iterator();
            } catch (final IOException err) {
                throw new java.io.UncheckedIOException(err);
            }
        }
    }
}
