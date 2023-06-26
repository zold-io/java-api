/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2023 Yegor Bugayenko
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
