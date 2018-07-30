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

import com.github.victornoel.eo.GenerateEnvelope;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * A payment transaction.
 *
 * @since 0.1
 */
@GenerateEnvelope
public interface Transaction {

    /**
     * Id of this transaction.
     * @return Id
     * @checkstyle MethodNameCheck (3 lines)
     */
    @SuppressWarnings("PMD.ShortMethodName")
    long id();

    /**
     * Timestamp of this transaction.
     * @return Time
     * @throws IOException When something goes wrong
     */
    ZonedDateTime time() throws IOException;

    /**
     * Amount involved in this transaction.
     * @return Amount
     */
    long amount();

    /**
     * Prefix.
     * @return Prefix
     * @throws IOException When something goes wrong
     */
    String prefix() throws IOException;

    /**
     * Beneficiary.
     * @return Beneficiary
     */
    long bnf();

    /**
     * Details.
     * @return Details
     */
    String details();

    /**
     * RSA Signature.
     * @return RSA Signature
     */
    String signature();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
