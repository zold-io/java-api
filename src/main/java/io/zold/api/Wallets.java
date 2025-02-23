/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2018-2025 Zerocracy
 * SPDX-License-Identifier: MIT
 */

package io.zold.api;

import java.io.IOException;

/**
 * Wallets.
 *
 * @since 0.1
 */
public interface Wallets extends Iterable<Wallet> {
    /**
     * Create a wallet.
     * @return The new wallet.
     * @throws IOException If an error occurs.
     */
    Wallet create() throws IOException;

    /**
     * Create a wallet.
     *
     * @param id The wallet id.
     * @param pubkey The wallet public key.
     * @param network The network the wallet belongs.
     * @return The new wallet.
     * @throws IOException If an error occurs.
     */
    Wallet create(final long id, final String pubkey, final String
        network) throws IOException;
}
