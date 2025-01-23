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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cactoos.collection.CollectionOf;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;
import org.llorllale.cactoos.matchers.FuncApplies;

/**
 * Test case for {@link Wallet}.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (3 lines)
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class WalletTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void readsWalletId() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        MatcherAssert.assertThat(wallet.id(), Matchers.is(id));
    }

    @Test
    public void throwRuntimeExceptionIfReadingIdFails() throws IOException {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> new Wallet.File(this.wallet("")).id()
        );
    }

    @Test
    public void throwNumberFormatExceptionIfIdIsInvalid() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                NumberFormatException.class,
                () -> new Wallet.File(this.wallet("invalid_id")).id()
            ).getMessage(),
            Matchers.startsWith("For input string:")
        );
    }

    // @todo #21:30min This test had to be marked as ignored after #30 because
    //  CpTransaction does not create the transaction string correctly yet.
    //  When CpTransaction is correctly implemented uncomment this method and
    //  make sure that it's working correctly
    @Test
    @Ignore
    public void pay() throws IOException {
        final Path path = this.folder.newFile().toPath();
        path.toFile().delete();
        Files.copy(this.wallet(5124095577148911L), path);
        final Wallet wallet = new Wallet.File(path);
        MatcherAssert.assertThat(
            wlt -> {
                wlt.pay(1, 1234);
                return wlt.ledger();
            },
            new FuncApplies<>(
                wallet,
                new IsIterableWithSize<Transaction>(
                    new IsEqual<>(new ListOf<>(wallet.ledger()).size() + 1)
                )
            )
        );
    }

    @Test
    public void mergesWallets() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("abcd;2017-07-19T21:25:07Z;0000000000a72366;xxsQuJa9;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(merged.ledger()).size(),
            new IsEqual<>(new CollectionOf<>(wallet.ledger()).size() + 1)
        );
    }

    @Test
    public void doesNotMergeWalletsWithDifferentId() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> wallet.merge(new Wallet.Fake(123L))
            ).getMessage(),
            Matchers.startsWith(
                "Wallet ID mismatch, ours is 123, theirs is 5124095577148911"
            )
        );
    }

    @Test
    public void doesNotMergeExistingTransactions() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(merged.ledger()).size(),
            new IsEqual<>(new CollectionOf<>(wallet.ledger()).size())
        );
    }

    @Test
    public void doesNotMergeTransactionsWithSameIdAndBnf() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-18T21:25:07Z;0000000000a72366;xxxxuuuu;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(merged.ledger()).size(),
            new IsEqual<>(new CollectionOf<>(wallet.ledger()).size())
        );
    }

    @Test
    public void doesNotMergeTransactionsWithSameIdAndNegativeAmount()
        throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-18T21:25:07Z;ffffffffffa72366;xxxxuuuu;98bb82c81735c4ff;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(merged.ledger()).size(),
            new IsEqual<>(new CollectionOf<>(wallet.ledger()).size())
        );
    }

    @Test
    public void doesNotMergeTransactionsWithSamePrefix() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("0011;2017-07-18T21:25:07Z;0000000000a72366;xksQuJa9;99bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new CollectionOf<>(merged.ledger()).size(),
            new IsEqual<>(new CollectionOf<>(wallet.ledger()).size())
        );
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void walletShouldBeAbleToReturnLedger() throws Exception {
        MatcherAssert.assertThat(
            new Wallet.File(this.wallet(5124095577148911L)).ledger(),
            Matchers.iterableWithSize(2)
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void keyIsNotYetImplemented() throws IOException {
        new Wallet.File(this.folder.newFile().toPath()).key();
    }

    private Path wallet(final long id) {
        return this.wallet(Long.toHexString(id));
    }

    private Path wallet(final String hex) {
        return Paths.get(
            String.format("src/test/resources/wallets/%s", hex)
        );
    }
}
