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
import org.cactoos.iterable.IterableOf;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link Network}.
 *
 * @since 0.1
 * @todo #5:30min Implement Remote interface. Remote Interface must be
 *  implemented because Network depends on Remote behavior. Network.pull
 *  needs to search all remotes for some wallet id and merge all found
 *  wallets; Network.push must push a wallet to a remote based in remote.
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (2 lines)
 */
public final class NetworkTest {

    @Test
    public void pushWalletToAllRemotes()  {
        final Remote highremote = Mockito.mock(Remote.class);
        final Remote lowremote = Mockito.mock(Remote.class);
        final Wallet wallet = Mockito.mock(Wallet.class);
        new RtNetwork(
            new IterableOf<Remote>(
                highremote, lowremote
            )
        ).push(wallet);
        Mockito.verify(
            highremote,
            Mockito.times(1)
        ).push(Mockito.any(Wallet.class));
        Mockito.verify(
            lowremote,
            Mockito.times(1)
        ).push(Mockito.any(Wallet.class));
    }

    @Test
    public void pullsWalletWithTheRightId() throws IOException {
        final long id = 1L;
        MatcherAssert.assertThat(
            new RtNetwork(
                new IterableOf<>(
                    new Remote.Fake(
                        new RtScore(new IterableOf<>(new TextOf("a")))
                    )
                )
            ).pull(id).id(),
            new IsEqual<>(id)
        );
    }
}
