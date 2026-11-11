/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.LengthOf;
import org.cactoos.scalar.Unchecked;
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
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
final class NetworkTest {

    @Test
    void pushesWalletToTheFirstRemote() {
        final Remote first = Mockito.mock(Remote.class);
        final Remote second = Mockito.mock(Remote.class);
        final Wallet wallet = Mockito.mock(Wallet.class);
        new RtNetwork(new IterableOf<Remote>(first, second)).push(wallet);
        Mockito.verify(first, Mockito.times(1)).push(Mockito.any(Wallet.class));
    }

    @Test
    void pushesWalletToTheSecondRemote() {
        final Remote first = Mockito.mock(Remote.class);
        final Remote second = Mockito.mock(Remote.class);
        final Wallet wallet = Mockito.mock(Wallet.class);
        new RtNetwork(new IterableOf<Remote>(first, second)).push(wallet);
        Mockito.verify(second, Mockito.times(1)).push(Mockito.any(Wallet.class));
    }

    @Test
    void pullsWalletWithTheRightId() throws IOException {
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

    @Test
    void loadsRemotesFromJsonFile() throws IOException {
        final Path file = Files.createTempFile("remotes", ".json");
        Files.write(
            file,
            "[{\"score\": 2}]".getBytes(StandardCharsets.UTF_8)
        );
        try {
            final int count = new Unchecked<>(
                new LengthOf(
                    new RtNetwork(
                        file,
                        new IterableOf<>(new Remote.Fake(1)),
                        json -> new Remote.Fake(json.getInt("score"))
                    )
                )
            ).value().intValue();
            MatcherAssert.assertThat(
                count,
                new IsEqual<>(2)
            );
        } finally {
            Files.delete(file);
        }
    }
}
