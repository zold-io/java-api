/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import com.github.victornoel.eo.GenerateEnvelope;
import org.cactoos.Text;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;

/**
 * A remote node's score, equal to its number of suffixes.
 *
 * The natural order of {@link Score} is from highest to lowest.
 *
 * Note: {@link Score} has a natural ordering that is inconsistent with equals.
 *
 * @since 0.1
 */
@GenerateEnvelope
public interface Score extends Comparable<Score> {

    /**
     * The suffixes associated with this score. Each suffix is a text of the
     * form {@code /[a-zA-Z0-9]+/}.
     * @return Suffixes for this score
     */
    Iterable<Text> suffixes();

    /**
     * Summary of multiple {@link Score}.
     *
     * @since 1.0
     */
    final class Summed extends ScoreEnvelope {
        /**
         * Ctor.
         *
         * @param scores Multiple scores to summary.
         */
        Summed(final Iterable<Score> scores) {
            super(new RtScore(
                new Joined<>(new Mapped<>(Score::suffixes, scores))
            ));
        }
    }
}
