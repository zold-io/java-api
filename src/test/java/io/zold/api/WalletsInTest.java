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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link WalletsIn}.
 *
 * @since 0.1
 * @todo #12:30min The name of the wallets in the test
 *  resource directory are not compliant with how
 *  wallets should be named (see white paper and WalletsIn).
 *  They should be made adequate and a test should be added to
 *  ensure WalletsIn.create does overwrite and existing wallet.
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
public final class WalletsInTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

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

    @Ignore("see @todo on WalletsIn.create")
    @Test
    public void createsWalletWithId() throws IOException {
        final long id = 1L;
        MatcherAssert.assertThat(
            "Can't create wallet with id",
            new WalletsIn(this.folder.newFolder().toPath()).create(id).id(),
            new IsEqual<>(id)
        );
    }

    @Test
    public void createsWalletInWallets() throws IOException {
        final Wallets wallets = new WalletsIn(this.folder.newFolder().toPath());
        wallets.create(1L);
        MatcherAssert.assertThat(
            "Can't create wallet in wallets",
            wallets,
            new IsIterableWithSize<>(new IsEqual<>(1))
        );
    }

    @Test
    public void createsWalletInFolder() throws IOException {
        final Path path = this.folder.newFolder().toPath();
        final long id = 1L;
        new WalletsIn(path).create(id);
        MatcherAssert.assertThat(
            "Can't create wallet in folder",
            Files.exists(
                path.resolve(
                    new JoinedText(".", Long.toHexString(id), "z").asString()
                )
            ),
            new IsEqual<>(true)
        );
    }
}
