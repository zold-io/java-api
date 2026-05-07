/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link WalletsIn}.
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class WalletsInTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void iteratesWallets() {
        MatcherAssert.assertThat(
            new WalletsIn(Paths.get("src/test/resources/walletsIn")),
            // @checkstyle MagicNumber (1 line)
            new IsIterableWithSize<>(new IsEqual<>(5))
        );
    }

    @Test
    public void createsWalletInWallets() throws IOException {
        final Wallets wallets = new WalletsIn(this.folder.newFolder().toPath());
        wallets.create();
        MatcherAssert.assertThat(
            "Can't create wallet in wallets",
            wallets,
            new IsIterableWithSize<>(new IsEqual<>(1))
        );
    }

    @Test
    public void createsWalletInFolder() throws IOException {
        final Path path = this.folder.newFolder().toPath();
        new WalletsIn(path).create();
        MatcherAssert.assertThat(
            "Can't create wallet in folder",
            new WalletsIn(path),
            new IsIterableWithSize<>(new IsEqual<>(1))
        );
    }

    @Test
    public void createsRightWallet() throws IOException {
        final Path path = this.folder.newFolder().toPath();
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> new WalletsIn(path).create(
                1L, "AAAAB3NzaC1yc2EAAAADAQABAAABAQC", "zold"
            )
        );
    }

    @Test
    public void doesNotOverwriteExistingWallet() throws Exception {
        final Path path = this.folder.newFolder().toPath();
        final Random random = new WalletsInTest.FkRandom(16_725L);
        new WalletsIn(path, random).create();
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new WalletsIn(path, random).create()
            ).getMessage(),
            Matchers.containsString("already exists")
        );
    }

    /**
     * Fake randomizer that returns the same value each time.
     * @since 1.0
     */
    private static final class FkRandom extends Random {

        /**
         * Serial version.
         */
        private static final long serialVersionUID = 2905348968220129619L;

        /**
         * Value that represents a random number.
         */
        private final long value;

        /**
         * Ctor.
         * @param val Value that represents a random number
         */
        FkRandom(final long val) {
            super();
            this.value = val;
        }

        @Override
        public long nextLong() {
            return this.value;
        }
    }
}
