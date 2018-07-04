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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.cactoos.io.LengthOf;
import org.cactoos.io.TeeInput;
import org.cactoos.text.JoinedText;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link Wallet}.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 */
public final class WalletTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void readsWalletId() throws IOException {
        final long id = 5124095577148911L;
        final Wallet wallet = new Wallet.File(this.wallet(id));
        MatcherAssert.assertThat(wallet.id(), Matchers.is(id));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void payIsNotYetImplemented() throws IOException {
        new Wallet.File(this.folder.newFile().toPath()).pay(1, 1234);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mergeIsNotYetImplemented() throws IOException {
        new Wallet.File(this.folder.newFile().toPath()).merge(
            new Wallet.File(this.folder.newFile().toPath())
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ledgerIsNotYetImplemented() throws IOException {
        new Wallet.File(this.folder.newFile().toPath()).ledger();
    }

    private Path wallet(final long id) throws IOException {
        final String hex = Long.toHexString(id);
        final File file = this.folder.newFile(hex);
        file.createNewFile();
        new LengthOf(
            new TeeInput(
                new JoinedText(
                    "\n",
                    "zold",
                    "1",
                    hex,
                    // @checkstyle LineLength (1 line)
                    "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGZCr/9hBChqsChd4sRAIpKNRinjhSW+J+S7PU5malVMiRHVoKjeooLDpWpij0A6vkzOvjrMldAZT0Fzgp0cJ15TOVwiQanQ5WuQDgRkLoxrdh/qyBApoDvk4OUEozOQPNwfpZOFfaUALPsPnv9995TlY9WcdSKW5dj041p1tJmlAgMBAAE="
                ),
                file
            )
        ).intValue();
        return file.toPath();
    }
}
