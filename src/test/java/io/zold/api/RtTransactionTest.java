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

import java.io.IOException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * Test case for {@link RtTransaction}.
 *
 * @since 0.1
 * @checkstyle LineLengthCheck (500 lines)
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.TooManyMethods"})
public final class RtTransactionTest {

    @Test
    public void shouldObeyEqualsHashcodeContract() {
        EqualsVerifier.forClass(RtTransaction.class)
            .withNonnullFields("transaction")
            .verify();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void idIsNotYetImplemented() {
        new RtTransaction("id()").id();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void timeIsNotYetImplemented() {
        new RtTransaction("time()").time();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void amountIsNotYetImplemented() {
        new RtTransaction("amount()").amount();
    }

    @Test
    public void returnsPrefix() throws IOException {
        MatcherAssert.assertThat(
            "Returned wrong prefix",
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix(),
            new IsEqual<>("xksQuJa9")
        );
    }

    @Test(expected = IOException.class)
    public void prefixFormatViolated() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;|invalidprefix|;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IOException.class)
    public void prefixSizeViolatedLess() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IOException.class)
    public void prefixSizeViolatedMore() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IOException.class)
    public void prefixNotPresent() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367"
        ).prefix();
    }

    @Test(expected = IOException.class)
    public void invalidTransactionString() throws IOException {
        new RtTransaction(
            "this is a invalid transaction String"
        ).prefix();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void bnfIsNotYetImplemented() {
        new RtTransaction("bnf()").bnf();
    }

    @Test
    public void returnsDetails() throws IOException {
        MatcherAssert.assertThat(
            "Returned wrong details",
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;Payment for 3 beers.;QCuLuVr4..."
            ).details(),
            new IsEqual<>("Payment for 3 beers.")
        );
    }

    @Test(expected = IOException.class)
    public void detailsFormatViolated() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;\\/^&;QCuLuVr4..."
        ).details();
    }

    @Test(expected = IOException.class)
    public void detailsSizeTooShort() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4D;98bb82c81735c4ee;;QCuLuVr4..."
        ).details();
    }

    @Test(expected = IOException.class)
    public void detailsSizeTooLong() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;u87d2hova0OhJdYTk5rWxRZeAn6keCBONkERVbcXeUfpZIadF2ncJ5BRNjDjyaPXHaBAmpckf1UZWMq4OKsaVkN9tAmpMm8Fisq3F7E4LWnNFxWdh1LNKlsn7DDvJmB926839C8MZXCfU26AhmC2pgfTUCEkcQHjXjRp4sCXpRRLfYrd134BVjvcq1jFhNDca8JQHutL5PDLagBD80ZLefEOcqoP0YJQrTLnJMQDIhGKHBkPp0NL5IBbRjzmVS4PiW5WV2qHFhUoe98PkMet1OGPsuLIN61ZdeaQwhwEAolqrPDkxELMNz9oF4Zn2uUdubFqvk8OXcPiChpMRlDiyfVMZLgKZaHyL0NqKlhSajFz4KhPNcApOwS2ODxJEJZ9v6HMEWgvVn0MnxrO9Mw6882mf0K7iEEHFUBpxVaeC6MJqQUfZ6YiSNXrulDrVkzcsJEoSLXiPKFpXecmQupJO4f5tv7t59VUFLgQVFIMVHQsyKu7IMNdvW9Akc05T61HE;QCuLuVr4..."
        ).details();
    }

    @Test(expected = IOException.class)
    public void detailsAbsentTransactionString() throws IOException {
        new RtTransaction(
            "003b;2017-07-19T21:25:08Z;ffffffffffa72367"
        ).details();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void signatureIsNotYetImplemented() {
        new RtTransaction("signature()").signature();
    }
}
