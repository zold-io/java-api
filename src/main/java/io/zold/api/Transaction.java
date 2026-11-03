/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import com.github.victornoel.eo.GenerateEnvelope;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * A payment transaction.
 * @since 0.1
 */
@GenerateEnvelope
public interface Transaction {

    /**
     * Id of this transaction.
     * @return Id ID
     * @throws IOException When something goes wrong
     * @checkstyle MethodNameCheck (3 lines)
     */
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
     * Fake implementation of Transaction, read from one ledger line.
     * @since 1.0
     */
    final class Fake extends TransactionEnvelope {

        /**
         * Constructor.
         * @param line Transaction line with seven fields separated by semicolons
         * @checkstyle ConstructorsCodeFreeCheck (3 lines)
         */
        public Fake(final String line) {
            super(new RtTransaction(line));
        }
    }
}
