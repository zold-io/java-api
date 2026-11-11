/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        super(
            new RtTransaction(
                String.format(
                    "%04x;%s;%016x;cGF5;%016x;Payment;%0684d",
                    0,
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                        ZonedDateTime.now(ZoneOffset.UTC)
                    ),
                    amt,
                    bnf,
                    0
                )
            )
        );
    }
}
