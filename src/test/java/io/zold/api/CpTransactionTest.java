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
