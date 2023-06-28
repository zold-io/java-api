/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2023 Zerocracy
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
 * @checkstyle ParameterNumberCheck (500 lines)
 */
@GenerateEnvelope
@SuppressWarnings("PMD.TooManyMethods")
public interface Transaction {

    /**
     * Id of this transaction.
     * @return Id ID
     * @throws IOException When something goes wrong
     * @checkstyle MethodNameCheck (3 lines)
     */
    @SuppressWarnings("PMD.ShortMethodName")
    int id() throws IOException;

    /**
     * Timestamp of this transaction.
     * @return Time
     * @throws IOException When something goes wrong
     */
    ZonedDateTime time() throws IOException;

    /**
     * Amount involved in this transaction.
     * @return Amount
     * @throws IOException When something goes wrong
     */
    long amount() throws IOException;

    /**
     * Prefix.
     * @return Prefix
     * @throws IOException When something goes wrong
     */
    String prefix() throws IOException;

    /**
     * Beneficiary.
     * @return Beneficiary
     * @throws IOException When something goes wrong
     */
    String bnf() throws IOException;

    /**
     * Details.
     * @return Details
     * @throws IOException When something goes wrong
     */
    String details() throws IOException;

    /**
     * RSA Signature.
     * @return RSA Signature
     * @throws IOException When something goes wrong
     */
    String signature() throws IOException;

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Fake implementation of Transaction.
     */
    final class Fake implements Transaction {

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

        /**
         * Constructor.
         *
         * @param id Transaction id
         * @param time Transaction time
         * @param amount Transaction amount
         * @param prefix Transaction prefix
         * @param bnf Transaction beneficiary
         * @param details Transaction details
         * @param signature Transaction signature
         * @todo #61:30min Too many parameters on Fake constructor.
         *  Transaction.Fake have too many parameters; think and implement a
         *  way of reducing this number. After this implementation correct
         *  all other Fake usages to receive the new paramater values.
         */
        public Fake(final int id, final ZonedDateTime time, final long
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
}
