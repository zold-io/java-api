/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
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
