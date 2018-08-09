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
import java.time.ZonedDateTime;

/**
 * Fake {@link Transaction} for usasge in tests.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ParameterNumberCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class FkTransaction implements Transaction {

    /**
     * Transaction id.
     */
    private final int id;
    /**
     * Datetime of the transaction.
     */
    private final ZonedDateTime time;
    /**
     * Transaction amount.
     */
    private final long amount;
    /**
     * Transaction prefix.
     */
    private final String prefix;
    /**
     * Transaction beneficiary.
     */
    private final String bnf;
    /**
     * Transaction details.
     */
    private final String details;
    /**
     * Transaction signature.
     */
    private final String signature;

    public FkTransaction(final int id, final ZonedDateTime time, final long
        amount, final String prefix, final String bnf, final String details,
        final String signature) {
        this.id = id;
        this.time = time;
        this.amount = amount;
        this.prefix = prefix;
        this.bnf = bnf;
        this.details = details;
        this.signature = signature;
    }

    @Override
    @SuppressWarnings("PMD.ShortMethodName")
    public int id() throws IOException {
        return this.id;
    }

    @Override
    public ZonedDateTime time() throws IOException {
        return this.time;
    }

    @Override
    public long amount() throws IOException {
        return this.amount;
    }

    @Override
    public String prefix() throws IOException {
        return this.prefix;
    }

    @Override
    public String bnf() throws IOException {
        return this.bnf;
    }

    @Override
    public String details() throws IOException {
        return this.details;
    }

    @Override
    public String signature() throws IOException {
        return this.signature;
    }
}
