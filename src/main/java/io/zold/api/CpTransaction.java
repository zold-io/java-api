/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
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
