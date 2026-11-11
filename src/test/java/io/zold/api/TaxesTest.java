/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.util.ArrayList;
import java.util.List;
import org.cactoos.text.FormattedText;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Taxes}.
 *
 * @since 1.0
 * @checkstyle MethodBodyCommentsCheck (500 lines)
 * @checkstyle AbbreviationAsWordInNameCheck (500 lines)
 */
final class TaxesTest {

    @Test
    void pay() throws Exception {
        final List<Transaction> ledger = new ArrayList<>(5);
        for (int index = 0; index < 5; ++index) {
            ledger.add(
                new Transaction.Fake(
                    new FormattedText(
                        "%04x;2017-07-%02dT21:24:51Z;%016x;dHJhbnNhY3Rpb24=;0000000000001000;transaction%d;c2lnbmF0dXJl",
                        index, 19 + index, 1024 * index, index
                    ).asString()
                )
            );
        }
        final List<Remote> remotes = new ArrayList<>(5);
        for (int counter = 0; counter < ledger.size(); ++counter) {
            remotes.add(
                new Remote.Fake(counter)
            );
        }
        final Wallet wallet = new Wallet.Fake(102_030, ledger);
        // @todo #61:30min Once Taxes.exec is implemented, replace this
        //  assertThrows with the original assertion that ensures the wallet
        //  ledger contains a transaction with the "TAXES" prefix.
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> new Taxes(remotes).exec(wallet)
        );
    }

    @Test
    void didntPayWhenLessThanOneZLDDebt() {
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> {
                throw new UnsupportedOperationException(
                    "selectedCorrectTaxReceiver() not yet supported"
                );
            }
        );
    }

    // @todo #61:30min Implement test assuring that the taxes had been paid
    //  correctly to the right nodes, according to whitepaper (p.13)each node.
    //  This payments must be checked by looking in the transactions of the
    //  wallet with "TAXES" prefix and must obey the rule set in #40 ("A first
    //  algorithm could pay the max to each node until there is nothing else
    //  to pay.").
    @Test
    void payToRightNodes() {
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> {
                throw new UnsupportedOperationException(
                    "payToRightNodes() not yet supported"
                );
            }
        );
    }
}
