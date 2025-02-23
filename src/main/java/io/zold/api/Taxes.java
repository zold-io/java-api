/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.Proc;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Taxes payment algorithm.
 *
 * @since 1.0
 * @todo #61:30min Implement tax payment to remote nodes.
 *  Payment should happen only if the wallet is in debt of more than
 *  1 Zold. Debt is difference between current taxes that should have
 *  been paid by wallet (see whitepaper for formula) and how much it
 *  already paid in the past. A first algorithm could pay the
 *  max to each node until there is nothing else to pay. After completion the
 *  test methods TaxesTest.pay and TaxesTest.didntPayIfLessThanOneZLDDebt
 *  new tests covering other possible situations must be added.
 */
public final class Taxes implements Proc<Wallet> {

    /**
     * The beneficiary nodes.
     */
    private final Iterable<Remote> bnfs;

    /**
     * Ctor.
     *
     * @param nodes Remote nodes.
     */
    public Taxes(final Iterable<Remote> nodes) {
        this.bnfs = new TaxBeneficiaries(nodes);
    }

    @Override
    public void exec(final Wallet wallet) {
        throw new UnsupportedOperationException(
            new UncheckedText(
                new FormattedText(
                    "paying taxes to %s not yet supported",
                    this.bnfs
                )
            ).asString()
        );
    }
}
