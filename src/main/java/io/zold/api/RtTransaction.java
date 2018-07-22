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
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;
import org.cactoos.scalar.ItemAt;
import org.cactoos.text.SplitText;
import org.cactoos.text.TextOf;

/**
 * RtTransaction.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
final class RtTransaction implements Transaction {

    /**
     * Pattern for transaction String.
     */
    private static final Pattern TRANSACTION = Pattern.compile(
        //@checkstyle LineLengthCheck (1 line)
        "^([A-Za-z0-9+\\/]{4})*([A-Za-z0-9+\\/]{4}|[A-Za-z0-9+\\/]{3}=|[A-Za-z0-9+\\/]{2}==)$"
    );

    /**
     * Pattern for amount String.
     */
    private static final Pattern AMT = Pattern.compile("[A-Fa-f0-9]{16}");

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

    @Override
    public long amount() throws IOException {
        try {
            final String amnt = new ItemAt<>(
                new SplitText(this.transaction, ";").iterator(),
                //@checkstyle MagicNumber (12 lines)
                2
            ).value().asString();
            if (!RtTransaction.AMT.matcher(amnt).matches()) {
                throw new IllegalArgumentException(
                    String.format(
                        // @checkstyle LineLength (1 line)
                        "Invalid amount '%s' expecting 64-bit signed hex string with 16 symbols",
                        amnt
                    )
                );
            }
            return new BigInteger(amnt, 16).longValue();
            //@checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception exception) {
            throw new IOException("Couldn't parse amount", exception);
        }
    }

    @Override
    public String prefix() throws IOException {
        try {
            final String prefix = new ItemAt<>(
                new SplitText(
                    new TextOf(
                        this.transaction
                    ),
                    new TextOf(";")
                ).iterator(),
                //@checkstyle MagicNumberCheck (1 line)
                3
            ).value().asString();
            //@checkstyle MagicNumberCheck (1 line)
            if (prefix.length() < 8 || prefix.length() > 32) {
                throw new IllegalArgumentException("Invalid prefix size");
            }
            if (!RtTransaction.TRANSACTION.matcher(prefix).matches()) {
                throw new IllegalArgumentException("Invalid base64 prefix");
            }
            return prefix;
            //@checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception exception) {
            throw new IOException(
                "Invalid prefix string",
                exception
            );
        }
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
