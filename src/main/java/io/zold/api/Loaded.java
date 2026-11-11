/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.io.UncheckedIOException;
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

/**
 * Loaded remotes.
 *
 * @since 1.0
 */
final class Loaded implements Iterable<Remote> {

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
     *
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
            return this.load().iterator();
        } catch (final IOException err) {
            throw new UncheckedIOException(err);
        }
    }

    private Iterable<Remote> load() throws IOException {
        final List<Remote> remotes = new ArrayList<>(0);
        try (
            JsonReader reader = Json.createReader(
                Files.newBufferedReader(this.file, StandardCharsets.UTF_8)
            )
        ) {
            reader.readArray().stream()
                .map(value -> (JsonObject) value)
                .map(this.loader)
                .forEach(remotes::add);
        }
        return remotes;
    }
}
