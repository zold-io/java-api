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
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import org.cactoos.Text;
import org.cactoos.iterable.LengthOf;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.ItemAt;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.FormattedText;
import org.cactoos.text.SplitText;
import org.cactoos.text.TextOf;
import org.cactoos.text.TrimmedText;
import org.cactoos.text.UncheckedText;
import org.cactoos.time.ZonedDateTimeOf;

/**
 * RtTransaction.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCoupling (3 lines)
 */
@SuppressWarnings({"PMD.AvoidCatchingGenericException",
    "PMD.AvoidFieldNameMatchingMethodName"})
final class RtTransaction implements Transaction {

    /**
     * Pattern for Prefix String.
     */
    private static final Pattern PREFIX = Pattern.compile(
        //@checkstyle LineLengthCheck (1 line)
        "^([A-Za-z0-9+\\/]{4})*([A-Za-z0-9+\\/]{4}|[A-Za-z0-9+\\/]{3}=|[A-Za-z0-9+\\/]{2}==)$"
    );

    /**
     * Pattern for 16 symbol hex string.
     */
    private static final Pattern HEX = Pattern.compile("[A-Fa-f0-9]{16}");

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
     * Pattern for ID String.
     */
    private static final Pattern IDENT = Pattern.compile("[A-Fa-f0-9]{4}");

    /**
     * String representation of transaction.
     */
    private final IoCheckedScalar<String> transaction;

    /**
     * Ctor.
     * @param trnsct String representation of transaction
     */
    RtTransaction(final String trnsct) {
        this.transaction = new IoCheckedScalar<>(
            new StickyScalar<>(
                () -> {
                    if (
                        new TrimmedText(
                            new TextOf(trnsct)
                        ).asString().isEmpty()
                    ) {
                        throw new IOException(
                            "Invalid transaction string: string is empty"
                        );
                    }
                    final List<Text> pieces =
                        new ListOf<>(
                            new SplitText(trnsct, ";")
                        );
                    // @checkstyle MagicNumberCheck (1 line)
                    if (new LengthOf(pieces).intValue() != 7) {
                        throw new IOException(
                            new FormattedText(
                                // @checkstyle LineLength (1 line)
                                "Invalid transaction string: expected 7 fields, but found %d",
                                pieces.size()
                            ).asString()
                        );
                    }
                    return trnsct;
                }
            )
        );
    }

    @Override
    @SuppressWarnings("PMD.ShortMethodName")
    public int id() throws IOException {
        final String ident = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    0, new SplitText(this.transaction.value(), ";")
                )
            ).value()
        ).asString();
        if (!RtTransaction.IDENT.matcher(ident).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        // @checkstyle LineLength (1 line)
                        "Invalid ID '%s' expecting 16-bit unsigned hex string with 4 symbols",
                        ident
                    )
                ).asString()
            );
        }
        // @checkstyle MagicNumber (1 line)
        return Integer.parseUnsignedInt(ident, 16);
    }

    @Override
    public ZonedDateTime time() throws IOException {
        return new ZonedDateTimeOf(
            new UncheckedText(
                new IoCheckedScalar<>(
                    new ItemAt<>(
                        1, new SplitText(this.transaction.value(), ";")
                    )
                ).value()
            ).asString(),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).value();
    }

    @Override
    public long amount() throws IOException {
        final String amnt = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    2, new SplitText(this.transaction.value(), ";")
                )
            ).value()
        ).asString();
        if (!RtTransaction.HEX.matcher(amnt).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        // @checkstyle LineLength (1 line)
                        "Invalid amount '%s' expecting 64-bit signed hex string with 16 symbols",
                        amnt
                    )
                ).asString()
            );
        }
        // @checkstyle MagicNumber (1 line)
        return new BigInteger(amnt, 16).longValue();
    }

    @Override
    public String prefix() throws IOException {
        final String prefix = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    //@checkstyle MagicNumberCheck (1 line)
                    3, new SplitText(this.transaction.value(), ";")
                )
            ).value()
        ).asString();
        //@checkstyle MagicNumberCheck (1 line)
        if (prefix.length() < 8 || prefix.length() > 32) {
            throw new IOException("Invalid prefix size");
        }
        if (!RtTransaction.PREFIX.matcher(prefix).matches()) {
            throw new IOException("Invalid base64 prefix");
        }
        return prefix;
    }

    @Override
    public String bnf() throws IOException {
        final String bnf = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    //@checkstyle MagicNumberCheck (1 line)
                    4, new SplitText(this.transaction.value(), ";")
                )
            ).value()
        ).asString();
        if (!RtTransaction.HEX.matcher(bnf).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        // @checkstyle LineLength (1 line)
                        "Invalid bnf string '%s', expecting hex string with 16 symbols",
                        bnf
                    )
                ).asString()
            );
        }
        return bnf;
    }

    @Override
    public String details() throws IOException {
        final String dtls = new UncheckedText(
            new IoCheckedScalar<>(
                new ItemAt<>(
                    //@checkstyle MagicNumberCheck (1 line)
                    5, new SplitText(this.transaction.value(), ";")
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
                    //@checkstyle MagicNumberCheck (1 line)
                    6, new SplitText(this.transaction.value(), ";")
                )
            ).value()
        ).asString();
        // @checkstyle MagicNumber (1 line)
        if (sign.length() != 684
            || !RtTransaction.SIGN.matcher(sign).matches()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        // @checkstyle LineLength (1 line)
                        "Invalid signature '%s', expecting base64 string with 684 characters",
                        sign
                    )
                ).asString()
            );
        }
        return sign;
    }

    @Override
    public String toString() {
        return new UncheckedScalar<>(this.transaction).value();
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
