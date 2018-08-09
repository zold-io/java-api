/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Yegor Bugayenko
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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.text.JoinedText;
import org.cactoos.time.ZonedDateTimeOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.Test;

/**
 * Test case for {@link Taxes}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 * @checkstyle MethodBodyCommentsCheck (500 lines)
 * @checkstyle AbbreviationAsWordInNameCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
public final class TaxesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void pay() throws Exception {
        final String prefix = "transaction";
        final String beneficiary = "4096";
        final ZonedDateTime first =
            new ZonedDateTimeOf(
                "2017-07-19T21:24:51Z",
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            ).value();
        final List<Transaction> ledger = new ArrayList<>(5);
        // @checkstyle AvoidInstantiatingObjectsInLoops (26 lines)
        for (int index = 0; index < 5; index = index + 1) {
            ledger.add(
                new FkTransaction(
                    index,
                    first.plusDays(index),
                    1024 * index,
                    prefix,
                    beneficiary,
                    new JoinedText(
                        "",
                        prefix,
                        Integer.toString(index)
                    ).asString(),
                    new JoinedText(
                        "",
                        "signature",
                        Integer.toString(index)
                    ).asString()
                )
            );
        }
        final List<Remote> remotes = new ArrayList<>(5);
        for (int counter = 0; counter < 5; counter = counter + 1) {
            remotes.add(
                new Remote.Fake(counter)
            );
        }
        final Wallet wallet = new Wallet.Fake(102030, ledger);
        new Taxes(remotes).exec(wallet);
        MatcherAssert.assertThat(
            "Didn't paid anything",
            wallet.ledger(),
            new IsCollectionContaining<Transaction>(
                // @todo #61:30min Create and implement an Transaction
                //  matcher, where we can assure if some transaction have
                // some values for its fields. After its implementation, fix
                // this test so it can assure that the wallet has at least
                // one transaction with TAXES prefix
                new IsEqual<>(
                    new StringContains("TAXES")
                )
            )
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void didntPayWhenLessThanOneZLDDebt() {
        throw new UnsupportedOperationException(
            "selectedCorrectTaxReceiver() not yet supported"
        );
    }

    // @todo #61:30min Implement test assuring that the taxes had been paid
    //  correctly to the right nodes, according to whitepaper (p.13)each node.
    //  This payments must be checked by looking in the transactions of the
    //  wallet with "TAXES" prefix and must obey the rule set in #40 ("A first
    //  algorithm could pay the max to each node until there is nothing else
    //  to pay.").
    @Test(expected = UnsupportedOperationException.class)
    public void payToRightNodes() {
        throw new UnsupportedOperationException(
            "payToRightNodes() not yet supported"
        );
    }
}

