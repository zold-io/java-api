/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;
import org.cactoos.list.Sorted;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * Test case for {@link Score}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class ScoreTest {

    @Test
    @SuppressWarnings("unchecked")
    public void fewerSuffixesComeAfter() {
        final Score lowest = new RtScore(
            new IterableOf<>(new TextOf("a"))
        );
        final Score highest = new RtScore(
            new IterableOf<>(new TextOf("b"), new TextOf("c"))
        );
        MatcherAssert.assertThat(
            new Sorted<>(lowest, highest),
            new IsIterableContainingInOrder<>(
                new ListOf<>(new IsEqual<>(highest), new IsEqual<>(lowest))
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sumWithFewerSuffixesComesAfter() {
        final Score lowest = new RtScore(
            new IterableOf<>(new TextOf("a"))
        );
        final Score highone = new RtScore(
            new IterableOf<>(new TextOf("b"), new TextOf("c"))
        );
        final Score hightwo = new RtScore(
            new IterableOf<>(new TextOf("d"), new TextOf("e"))
        );
        final Score sum = new Score.Summed(new IterableOf<>(lowest, highone));
        MatcherAssert.assertThat(
            new Sorted<>(sum, hightwo),
            new IsIterableContainingInOrder<>(
                new ListOf<>(
                    new IsEqual<>(sum),
                    new IsEqual<>(hightwo)
                )
            )
        );
    }
}
