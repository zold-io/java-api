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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import org.cactoos.collection.Filtered;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Skipped;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.CheckedScalar;
import org.cactoos.scalar.Or;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.FormattedText;
import org.cactoos.text.SplitText;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Wallet.
 * @since 0.1
 */
@SuppressWarnings({"PMD.ShortMethodName", "PMD.TooManyMethods"})
public interface Wallet {
    /**
     * This wallet's ID: an unsigned 64-bit integer.
     * @return This wallet's id
     * @throws IOException If an IO error occurs
     * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
     * @checkstyle MethodName (2 lines)
     */
    long id() throws IOException;

    /**
     * Make a payment.
     * @param amt Amount to pay in zents
     * @param bnf Wallet ID of beneficiary
     * @throws IOException If an IO error occurs
     */
    void pay(long amt, long bnf) throws IOException;

    /**
     * Merge both {@code this} and {@code other}. Fails if they are not the
     * same wallet, as identified by their {@link #id() id}.
     * @param other Other wallet
     * @return The merged wallet
     * @throws IOException If an IO error occurs
     */
    Wallet merge(Wallet other) throws IOException;

    /**
     * This wallet's ledger.
     * @return This wallet's ledger
     */
    Iterable<Transaction> ledger();

    /**
     * A Fake {@link Wallet}.
     * @since 1.0
     */
    final class Fake implements Wallet {

        /**
         * The wallet id.
         */
        private final long id;

        /**
         * Transactions.
         */
        private final Iterable<Transaction> transactions;

        /**
         * Ctor.
         * @param id The wallet id.
         */
        public Fake(final long id) {
            this(id, new IterableOf<>());
        }

        /**
         * Ctor.
         * @param id The wallet id.
         * @param transactions Transactions.
         */
        public Fake(final long id, final Transaction... transactions) {
            this(id, new IterableOf<>(transactions));
        }

        /**
         * Ctor.
         * @param id The wallet id.
         * @param transactions Transactions.
         */
        public Fake(final long id, final Iterable<Transaction> transactions) {
            this.id = id;
            this.transactions = transactions;
        }

        @Override
        public long id() throws IOException {
            return this.id;
        }

        @Override
        public void pay(final long amt, final long bnf) {
            // nothing
        }

        @Override
        public Wallet merge(final Wallet other) {
            return other;
        }

        @Override
        public Iterable<Transaction> ledger() {
            return this.transactions;
        }
    }

    /**
     * Default File implementation.
     * @checkstyle ClassDataAbstractionCouplingCheck (2 lines)
     */
    final class File implements Wallet {

        /**
         * Path of this wallet.
         */
        private final Path path;

        /**
         * Ctor.
         * @param path Path of wallet
         */
        File(final Path path) {
            this.path = path;
        }

        @Override
        public long id() throws IOException {
            return new CheckedScalar<>(
                () -> Long.parseUnsignedLong(
                    new ListOf<>(
                        new SplitText(
                            new TextOf(this.path),
                            "\n"
                        )
                    ).get(2).asString(),
                    // @checkstyle MagicNumber (1 line)
                    16
                ),
                e -> new IOException(e)
            ).value();
        }

        @Override
        public void pay(final long amt, final long bnf) throws IOException {
            try (final Writer out = new FileWriter(this.path.toFile(), true)) {
                out.write('\n');
                out.write(new CpTransaction(amt, bnf).toString());
            }
        }

        @Override
        // @todo #16:30min Following transactions should be ignored according
        //  to the whitepaper:
        //  a) If the transaction is negative and its signature is not valid,
        //  it is ignored;
        //  b) If the transaction makes the balance of the wallet negative,
        //  it is ignored;
        //  c) If the transaction is positive and it’s absent in the paying
        //  wallet (which exists at the node), it’s ignored; If the paying
        //  wallet doesn’t exist at the node, the transaction is ignored;
        //
        // @todo: #16:30min Merge method should update transactions
        // in wallet's file and return concrete implementation not a fake one.
        // Beware that tests should be refactored to take care of file cleanup
        // after each case that merges wallets.
        //
        public Wallet merge(final Wallet other) throws IOException {
            if (other.id() != this.id()) {
                throw new IOException(
                    new UncheckedText(
                        new FormattedText(
                            "Wallet ID mismatch, ours is %d, theirs is %d",
                            other.id(),
                            this.id()
                        )
                    ).asString()
                );
            }
            final Iterable<Transaction> ledger = this.ledger();
            final Collection<Transaction> candidates = new ArrayList<>(0);
            for (final Transaction remote : other.ledger()) {
                final Collection<Transaction> filtered =
                    new Filtered<>(
                        input -> new UncheckedScalar<>(
                            new Or(
                                () -> remote.equals(input),
                                () -> remote.id() == input.id()
                                    && remote.bnf().equals(input.bnf()),
                                () -> remote.id() == input.id()
                                    && remote.amount() < 0L,
                                () -> remote.prefix().equals(input.prefix())
                            )
                        ).value(),
                        ledger
                    );
                if (filtered.isEmpty()) {
                    candidates.add(remote);
                }
            }
            return new Wallet.Fake(
                this.id(),
                new Joined<Transaction>(ledger, candidates)
            );
        }

        @Override
        public Iterable<Transaction> ledger() {
            return new Mapped<>(
                txt -> new RtTransaction(txt.asString()),
                new Skipped<>(
                    new ListOf<>(
                        new SplitText(
                            new TextOf(this.path),
                            "\\n"
                        )
                    ),
                    // @checkstyle MagicNumberCheck (1 line)
                    5
                )
            );
        }
    }
}
