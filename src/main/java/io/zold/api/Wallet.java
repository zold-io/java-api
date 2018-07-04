/**
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
import java.nio.file.Path;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.SyncScalar;
import org.cactoos.text.SplitText;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Wallet.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@SuppressWarnings("PMD.ShortMethodName")
public interface Wallet {
    /**
     * This wallet's ID: an unsigned 64-bit integer.
     * @return This wallet's id
     * @throws IOException If an IO error occurs.
     * @checkstyle MethodName (2 lines)
     */
    long id() throws IOException;

    /**
     * Make a payment.
     * @param amt Amount to pay in zents
     * @param bnf Wallet ID of beneficiary
     */
    void pay(long amt, long bnf);

    /**
     * Merge both {@code this} and {@code other}. Fails if they are not the
     * same wallet, as identified by their {@link #id() id}.
     * @param other Other wallet
     * @return The merged wallet
     */
    Wallet merge(Wallet other);

    /**
     * This wallet's ledger.
     * @return This wallet's ledger
     */
    Iterable<Transaction> ledger();

    /**
     * Default File implementation.
     */
    class File implements Wallet {

        /**
         * Path of this wallet.
         */
        private final IoCheckedScalar<Path> path;

        /**
         * Ctor.
         * @param path Path of wallet
         */
        File(final Path path) {
            this(() -> path);
        }

        /**
         * Ctor.
         * @param pth Path of wallet
         */
        File(final Scalar<Path> pth) {
            this.path = new IoCheckedScalar<>(
                new SyncScalar<>(
                    new StickyScalar<>(pth)
                )
            );
        }

        @Override
        public long id() throws IOException {
            return Long.parseUnsignedLong(
                new UncheckedText(
                    new ListOf<>(
                        new SplitText(
                            new TextOf(
                                this.path.value()
                            ),
                            "\n"
                        )
                    ).get(2)
                ).asString(),
                // @checkstyle MagicNumber (1 line)
                16
            );
        }

        // @todo #6:30min Implement pay method. This should add a transaction
        //  to the wallet containing the correct details. Also add a unit test
        //  to replace WalletTest.payIsNotYetImplemented().
        @Override
        public void pay(final long amt, final long bnf) {
            throw new UnsupportedOperationException("pay() not yet supported");
        }

        // @todo #6:30min Implement merge method. This should merge this wallet
        //  with a copy of the same wallet. It should throw an error if a
        //  wallet is provided. Also add a unit test to replace
        //  WalletTest.mergeIsNotYetImplemented().
        @Override
        public Wallet merge(final Wallet other) {
            throw new UnsupportedOperationException(
                "merge() not yet supported"
            );
        }

        // @todo #6:30min Implement ledger method. This should return all
        //  the transactions in this copy of the wallet. Also add a unit test
        //  to replace WalletTest.ledgerIsNotYetImplemented().
        @Override
        public Iterable<Transaction> ledger() {
            throw new UnsupportedOperationException(
                "ledger() not yet supported"
            );
        }
    }
}
