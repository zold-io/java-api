/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;

/**
 * Network of remote nodes.
 *
 * @since 0.1
 */
public interface Network extends Iterable<Remote> {

    /**
     * Push the wallet to the network.
     * @param wallet The wallet
     */
    void push(Wallet wallet);

    /**
     * Pull a wallet from the network.
     * @param id The wallet's {@link Wallet#id() id}
     * @return The wallet
     * @throws IOException If an IO error occurs
     */
    Wallet pull(long id) throws IOException;

}
