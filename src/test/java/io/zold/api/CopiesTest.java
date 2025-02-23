/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.collection.CollectionOf;
import org.cactoos.iterable.IterableOf;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Copies}.
 *
 * @since 1.0
 * @todo #56:30min Add more test scenarios to Copies.
 *  Scenarios:
 *  remotes return empty list
 *  remotes return single element
 *  remotes return wallets with different content
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class CopiesTest {

    @Test
    public void createsOneCopy() {
        final Iterable<Copies.Copy> copies = new Copies(
            1L,
            new IterableOf<>(
                new Remote.Fake(new RtScore(new IterableOf<>(new TextOf("a")))),
                new Remote.Fake(new RtScore(new IterableOf<>(new TextOf("b"))))
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(copies).size(),
            new IsEqual<>(1)
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(
                copies.iterator().next().score().suffixes()
            ).size(),
            new IsEqual<>(2)
        );
    }
}
