/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2025 Zerocracy
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
