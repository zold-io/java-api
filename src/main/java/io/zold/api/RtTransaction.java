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

/**
 * RtTransaction.
 *
 * @since 0.1
 */
final class RtTransaction implements Transaction {

    /**
     * String representation of transaction.
     */
    private final String transaction;

    /**
     * Ctor.
     * @param trnsct String representation of transaction
     * @todo #15:30min Check validity of the incoming string representation
     *  of transaction. It should comply the pattern described in the white
     *  paper. Cover with unit tests.
     */
    RtTransaction(final String trnsct) {
        this.transaction = trnsct;
    }

    // @todo #15:30min Implement id() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    @SuppressWarnings("PMD.ShortMethodName")
    public long id() {
        throw new UnsupportedOperationException("id() not yet implemented");
    }

    // @todo #15:30min Implement time() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public ZonedDateTime time() {
        throw new UnsupportedOperationException("time() not yet implemented");
    }

    // @todo #15:30min Implement amount() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public long amount() {
        throw new UnsupportedOperationException("amount() not yet implemented");
    }

    // @todo #15:30min Implement prefix() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public String prefix() {
        throw new UnsupportedOperationException("prefix() not yet implemented");
    }

    // @todo #15:30min Implement bnf() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public long bnf() {
        throw new UnsupportedOperationException("bnf() not yet implemented");
    }

    // @todo #15:30min Implement details() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public String details() {
        throw new UnsupportedOperationException(
            "details() not yet implemented"
        );
    }

    // @todo #15:30min Implement signature by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public String signature() {
        throw new UnsupportedOperationException(
            "signature() not yet implemented"
        );
    }

    @Override
    public String toString() {
        return this.transaction;
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final RtTransaction that = (RtTransaction) obj;
        return this.transaction.equals(that.transaction);
    }

    @Override
    public int hashCode() {
        return this.transaction.hashCode();
    }
}
