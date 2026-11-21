/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test case for {@link WalletsIn}.
 *
 * @since 0.1
 */
final class WalletsInTest {

    @Test
    void iteratesWallets() {
        MatcherAssert.assertThat(
            new WalletsIn(Paths.get("src/test/resources/walletsIn")),
            new IsIterableWithSize<>(new IsEqual<>(5))
        );
    }

    @Test
    void createsWalletInWallets(@TempDir final Path folder) throws IOException {
        final Wallets wallets = new WalletsIn(Files.createTempDirectory(folder, "wlt"));
        wallets.create();
        MatcherAssert.assertThat(
            "Can't create wallet in wallets",
            wallets,
            new IsIterableWithSize<>(new IsEqual<>(1))
        );
    }

    @Test
    void createsWalletInFolder(@TempDir final Path folder) throws IOException {
        final Path path = Files.createTempDirectory(folder, "wlt");
        new WalletsIn(path).create();
        MatcherAssert.assertThat(
            "Can't create wallet in folder",
            new WalletsIn(path),
            new IsIterableWithSize<>(new IsEqual<>(1))
        );
    }

    @Test
    @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
    void createsRightWallet(@TempDir final Path folder) throws IOException {
        final Path path = Files.createTempDirectory(folder, "wlt");
        final Wallet wallet = new WalletsIn(path).create(
            1L, "AAAAB3NzaC1yc2EAAAADAQABAAABAQC", "zold"
        );
        Assertions.assertEquals(1L, wallet.id());
        Assertions.assertEquals(
            "AAAAB3NzaC1yc2EAAAADAQABAAABAQC", wallet.key()
        );
        try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
            Assertions.assertEquals(
                "zold", Files.readAllLines(files.iterator().next()).get(0)
            );
        }
    }

    @Test
    void doesNotOverwriteExistingWallet(@TempDir final Path folder) throws Exception {
        final Path path = Files.createTempDirectory(folder, "wlt");
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
     *
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
         *
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
