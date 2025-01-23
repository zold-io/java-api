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

/**
 * Computed Transaction.
 *
 * @since 1.0
 * @todo #54:30min Implement the computation of the transaction string
 *  based on the white paper. The unit tests should also be updated to
 *  ensure it works as expected and test for
 *  returnSignatureForNegativeTransaction must be implemented.
 */
public final class CpTransaction extends TransactionEnvelope {

    /**
     * Ctor.
     *
     * @param amt Amount to pay in zents
     * @param bnf Wallet ID of beneficiary
     */
    CpTransaction(final long amt, final long bnf) {
        super(new RtTransaction(Long.toString(amt + bnf)));
    }
}
