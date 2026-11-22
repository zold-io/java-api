/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.llorllale.cactoos.matchers.IsApplicable;

/**
 * Test case for {@link Wallet}.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class WalletTest {

    @Test
    void readsWalletId() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        MatcherAssert.assertThat(wallet.id(), Matchers.is(id));
    }

    @Test
    void throwRuntimeExceptionIfReadingIdFails() throws IOException {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> new Wallet.File(this.wallet("")).id()
        );
    }

    @Test
    void throwNumberFormatExceptionIfIdIsInvalid() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                NumberFormatException.class,
                () -> new Wallet.File(this.wallet("invalid_id")).id()
            ).getMessage(),
            Matchers.startsWith("For input string:")
        );
    }

    @Test
    void pay(@TempDir final Path folder) throws IOException {
        final Path path = Files.createTempFile(folder, "wlt", ".tmp");
        path.toFile().delete();
        Files.copy(this.wallet(5_124_095_577_148_911L), path);
        final Wallet wallet = new Wallet.File(path);
        MatcherAssert.assertThat(
            wlt -> {
                wlt.pay(1, 1234);
                return wlt.ledger();
            },
            new IsApplicable<>(
                wallet,
                new IsIterableWithSize<Transaction>(
                    new IsEqual<>(new ListOf<>(wallet.ledger()).size() + 1)
                )
            )
        );
    }

    @Test
    void doesNotMergePositiveTransactionWithoutPayingWallet()
        throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("abcd;2017-07-19T21:25:07Z;0000000000a72366;xxsQuJa9;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(new ListOf<>(wallet.ledger()).size())
        );
    }

    @Test
    void doesNotMergeWalletsWithDifferentId() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> wallet.merge(new Wallet.Fake(123L))
            ).getMessage(),
            Matchers.startsWith(
                "Wallet ID mismatch, ours is 5124095577148911, theirs is 123"
            )
        );
    }

    @Test
    void doesNotMergeExistingTransactions() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(new ListOf<>(wallet.ledger()).size())
        );
    }

    @Test
    void doesNotMergeTransactionsWithSameIdAndBnf() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-18T21:25:07Z;0000000000a72366;xxxxuuuu;98bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(new ListOf<>(wallet.ledger()).size())
        );
    }

    @Test
    void doesNotMergeTransactionsWithSameIdAndNegativeAmount()
        throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("003b;2017-07-18T21:25:07Z;ffffffffffa72366;xxxxuuuu;98bb82c81735c4ff;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(new ListOf<>(wallet.ledger()).size())
        );
    }

    @Test
    void doesNotMergeTransactionsWithSamePrefix() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                //@checkstyle LineLengthCheck (1 lines)
                new RtTransaction("0011;2017-07-18T21:25:07Z;0000000000a72366;xksQuJa9;99bb82c81735c4ee;For food;QCuLuVr4...")
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(new ListOf<>(wallet.ledger()).size())
        );
    }

    @Test
    void doesNotMergeInvalidTransactions() throws IOException {
        final long id = 5_124_095_577_148_911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        final int size = new ListOf<>(wallet.ledger()).size();
        final Wallet merged = wallet.merge(
            new Wallet.Fake(
                id,
                new RtTransaction(
                    "0040;2017-07-18T21:25:07Z;ffffffffffffffff;xxxxuuuu;98bb82c81735c4ee;For food;bad"
                ),
                new RtTransaction(
                    "0041;2017-07-18T21:25:07Z;8000000000000000;xxxxuuuu;98bb82c81735c4ee;For food;bad"
                ),
                new RtTransaction(
                    "0042;2017-07-18T21:25:07Z;0000000000000001;xxxxuuuu;98bb82c81735c4ee;For food;bad"
                )
            )
        );
        MatcherAssert.assertThat(
            new ListOf<>(merged.ledger()).size(),
            new IsEqual<>(size)
        );
    }

    @Test
    void walletShouldBeAbleToReturnLedger() throws Exception {
        MatcherAssert.assertThat(
            new Wallet.File(this.wallet(5_124_095_577_148_911L)).ledger(),
            Matchers.iterableWithSize(2)
        );
    }

    @Test
    void readsPublicKey() throws IOException {
        MatcherAssert.assertThat(
            new Wallet.File(this.wallet(5_124_095_577_148_911L)).key(),
            Matchers.is(
                "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGZCr/9hBChqsChd4sRAIpKNRinjhSW+J+S7PU5malVMiRHVoKjeooLDpWpij0A6vkzOvjrMldAZT0Fzgp0cJ15TOVwiQanQ5WuQDgRkLoxrdh/qyBApoDvk4OUEozOQPNwfpZOFfaUALPsPnv9995TlY9WcdSKW5dj041p1tJmlAgMBAAE="
            )
        );
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
