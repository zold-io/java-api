/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link CpTransaction}.
 *
 * @since 1.0
 * @checkstyle LineLengthCheck (500 lines)
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 */
public final class CpTransactionTest {

    @Test
    @Disabled
    public void returnAmount() throws IOException {
        final long amount = 256;
        MatcherAssert.assertThat(
            "Cannot return amount",
            new CpTransaction(amount, 1024).amount(),
            new IsEqual<>(256)
        );
    }

    @Test
    @Disabled
    public void returnSignatureForPositiveTransaction() throws IOException {
        final long id = 1024;
        MatcherAssert.assertThat(
            "Cannot return signature",
            new CpTransaction(256, id).signature(),
            new IsEqual<>("1024")
        );
    }

    @Test
    @Disabled
    public void returnPrefix() throws IOException {
        final long id = 1024;
        final Wallet wallet = new Wallet.Fake(id);
        MatcherAssert.assertThat(
            "Cannot return prefix",
            wallet.key(),
            new StringContains(new CpTransaction(256, id).prefix())
        );
    }

    @Test
    @Disabled
    public void returnBeneficiary() throws IOException {
        final long id = 1024;
        final Wallet wallet = new Wallet.Fake(id);
        MatcherAssert.assertThat(
            "Cannot return beneficiary",
            new CpTransaction(256, id).bnf(),
            new IsEqual<>(wallet.key())
        );
    }
}
