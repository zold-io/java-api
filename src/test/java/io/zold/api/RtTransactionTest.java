/**
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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * Test case for {@link RtTransaction}.
 *
 * @author Izbassar Tolegen (t.izbassar@gmail.com)
 * @version $Id$
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
    public void returnsPrefix() throws Exception {
        MatcherAssert.assertThat(
            "Returned wrong prefix",
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix(),
            new IsEqual<>("xksQuJa9")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixFormatViolated() throws Exception {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;|invalidprefix|;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixSizeViolatedLess() throws Exception {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixSizeViolatedMore() throws Exception {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
        ).prefix();
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixNotPresent() throws Exception {
        new RtTransaction(
            "003b;2017-07-19T21:25:07Z;ffffffffffa72367"
        ).prefix();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void bnfIsNotYetImplemented() {
        new RtTransaction("bnf()").bnf();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void detailsIsNotYetImplemented() {
        new RtTransaction("details()").details();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void signatureIsNotYetImplemented() {
        new RtTransaction("signature()").signature();
    }
}
