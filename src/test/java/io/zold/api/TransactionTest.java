/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Transaction}.
 *
 * @since 1.0
 */
final class TransactionTest {

    @Test
    void readsDetailsOfFakeFromOneLine() throws Exception {
        MatcherAssert.assertThat(
            "Fake transaction did not read details from its line",
            new Transaction.Fake(
                "03e1;2018-02-11T09:41:05Z;00000000001f7a2c;cGF5bWVudA==;7c0de10f8a2b9e41;Rent for May;Zm9vYmFy"
            ).details(),
            new IsEqual<>("Rent for May")
        );
    }
}
