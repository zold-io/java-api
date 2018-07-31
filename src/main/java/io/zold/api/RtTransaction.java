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
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.ItemAt;
import org.cactoos.text.FormattedText;
import org.cactoos.text.SplitText;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import org.cactoos.time.ZonedDateTimeOf;

/**
 * RtTransaction.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCoupling (3 lines)
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
     * Pattern for parsing Signature.
     */
    private static final Pattern SIGN = Pattern.compile("[A-Za-z0-9+/]+={0,3}");

    /**
     * Pattern for Details string.
     */
    private static final Pattern DTLS =
        Pattern.compile("[A-Za-z0-9 -.]{1,512}");

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

    @Override
    public ZonedDateTime time() throws IOException {
        return new ZonedDateTimeOf(
            new UncheckedText(
                new IoCheckedScalar<>(
                    new ItemAt<>(
                        1, new SplitText(this.transaction, ";")
                    )
                ).value()
            ).asString(),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).value();
    }

    // @todo #15:30min Implement amount() by parsing the string representation
    //  of transaction according to the pattern, described in the white
    //  paper. Replace relevant test case with actual tests.
    @Override
    public long amount() {
        throw new UnsupportedOperationException("amount() not yet implemented");
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

    @Override
    public String details() throws IOException {
        final String dtls = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    // @checkstyle MagicNumber (1 line)
                    5, new SplitText(this.transaction, ";")
                )
            ).value()
        ).asString();
        if (!RtTransaction.DTLS.matcher(dtls).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        // @checkstyle LineLength (1 line)
                        "Invalid details string '%s', does not match pattern '%s'",
                        dtls, RtTransaction.DTLS
                    )
                ).asString()
            );
        }
        return dtls;
    }

    @Override
    public String signature() throws IOException {
        final String sign = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    // @checkstyle MagicNumber (1 line)
                    6, new SplitText(this.transaction, ";")
                )
            ).value()
        ).asString();
        if (!RtTransaction.SIGN.matcher(sign).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        "Invalid signature '%s' expecting base64 string",
                        sign
                    )
                ).asString()
            );
        }
        return sign;
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
