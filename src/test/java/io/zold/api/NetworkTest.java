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

import java.util.ArrayList;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Repeated;
import org.cactoos.text.RandomText;
import org.junit.Ignore;
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
 */
public final class NetworkTest {

    @Test
    @Ignore
    public void pushWalletToRightRemote()  {
        final Remote highremote = Mockito.mock(Remote.class);
        final Score highscore = Mockito.mock(Score.class);
        Mockito.when(highscore.suffixes()).thenReturn(
            new Repeated<>(20, new RandomText())
        );
        Mockito.when(highremote.score()).thenReturn(highscore);
        final Remote lowremote = Mockito.mock(Remote.class);
        final Score lowscore = Mockito.mock(Score.class);
        Mockito.when(lowscore.suffixes()).thenReturn(
            new Repeated<>(18, new RandomText())
        );
        Mockito.when(lowremote.score()).thenReturn(lowscore);
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
            Mockito.never()
        ).push(Mockito.any(Wallet.class));
    }

    @Test
    @Ignore
    public void filtersUnqualifiedRemotesFromPush() {
        final Remote remote = Mockito.mock(Remote.class);
        final Score score = Mockito.mock(Score.class);
        Mockito.when(score.suffixes()).thenReturn(
            new Repeated<>(15, new RandomText())
        );
        Mockito.when(remote.score()).thenReturn(score);
        final Wallet wallet = Mockito.mock(Wallet.class);
        new RtNetwork(
            new IterableOf<Remote>(remote)
        ).push(wallet);
        Mockito.verify(
            remote,
            Mockito.never()
        ).push(Mockito.any(Wallet.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void pullNotYetSupported() {
        new RtNetwork(new ArrayList<>(1)).pull(1L);
    }

}
