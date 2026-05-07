/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.Text;
import org.cactoos.scalar.LengthOf;
import org.cactoos.scalar.Unchecked;

/**
 * Default implementation for {@link Score}.
 * @since 1.0
 */
public final class RtScore implements Score {

    /**
     * The suffixes.
     */
    private final Iterable<Text> sfxs;

    /**
     * Ctor.
     * @param sfxs The suffixes
     */
    RtScore(final Iterable<Text> sfxs) {
        this.sfxs = sfxs;
    }

    @Override
    public int compareTo(final Score other) {
        return new Unchecked<>(new LengthOf(other.suffixes())).value().intValue()
            - new Unchecked<>(new LengthOf(this.sfxs)).value().intValue();
    }

    @Override
    public Iterable<Text> suffixes() {
        return this.sfxs;
    }
}
