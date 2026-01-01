/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * Test case for {@link TaxBeneficiaries}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 */
public final class TaxBeneficiariesTest {

    @Test
    @SuppressWarnings("unchecked")
    public void sorts() {
        final Remote highremote = new Remote.Fake(20);
        final Remote lowremote = new Remote.Fake(18);
        MatcherAssert.assertThat(
            "Can't sort",
            new TaxBeneficiaries(
                new IterableOf<>(lowremote, highremote)
            ),
            new IsIterableContainingInOrder<>(
                new ListOf<>(
                    new IsEqual<>(highremote),
                    new IsEqual<>(lowremote)
                )
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void filters() {
        final Remote highremote = new Remote.Fake(20);
        final Remote vrylowremote = new Remote.Fake(14);
        MatcherAssert.assertThat(
            "Can't filter",
            new TaxBeneficiaries(
                new IterableOf<>(vrylowremote, highremote)
            ),
            new IsIterableContainingInOrder<>(
                new ListOf<>(new IsEqual<>(highremote))
            )
        );
    }
}
