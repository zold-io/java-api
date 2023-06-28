/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2023 Zerocracy
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
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
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

    @Test(expected = UnsupportedOperationException.class)
    public void createsRightWallet() throws IOException {
        final Path path = this.folder.newFolder().toPath();
        final String network = "zold";
        final String pubkey = "AAAAB3NzaC1yc2EAAAADAQABAAABAQC";
        final long id = 1;
        final Wallet actual = new WalletsIn(path).create(
            id, pubkey, network
        );
        final Wallet expected = new Wallet.Fake(
            id,
            pubkey,
            network
        );
        MatcherAssert.assertThat(
            "Created wallet with different values than expected",
            actual,
            new IsEqual<>(expected)
        );
    }

    @Test
    public void doesNotOverwriteExistingWallet() throws Exception {
        final Path path = this.folder.newFolder().toPath();
        final Random random = new FkRandom(16725L);
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
     */
    private static class FkRandom extends Random {

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
         * @param val Value that represents a random number.
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
