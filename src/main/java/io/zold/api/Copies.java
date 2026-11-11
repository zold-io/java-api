/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;

/**
 * Multiple copies of a Wallet.
 *
 * @since 1.0
 */
public final class Copies extends IterableEnvelope<Copy> {

    /**
     * Ctor.
     *
     * @param id Id of the wallet to pull
     * @param remotes Remote nodes
     */
    Copies(final long id, final Iterable<Remote> remotes) {
        super(new IterableOf<>(() -> copies(id, remotes).iterator()));
    }

    private static Iterable<Copy> copies(final long id,
        final Iterable<Remote> remotes) throws IOException {
        final List<Copy> copies = new ArrayList<>(10);
        for (final Remote remote : remotes) {
            final Wallet wallet = remote.pull(id);
            boolean updated = false;
            for (int idx = 0; idx < copies.size(); idx += 1) {
                final Copy copy = copies.get(idx);
                if (Copies.equalWallets(copies.get(idx).wallet(), wallet)) {
                    copies.set(idx, copy.with(remote));
                    updated = true;
                }
            }
            if (!updated) {
                copies.add(new Copy(wallet, remote));
            }
        }
        return new IterableOf<>(copies.iterator());
    }

    // @todo #56:30min Compare the entire content of two wallets. In addition to
    // id, compare RSA key and all transactions one by one. Entire content of
    // each transaction should be compared.
    private static boolean equalWallets(final Wallet first,
        final Wallet second) throws IOException {
        return first.id() == second.id() && new ListOf<>(
            first.ledger()
        ).size() == new ListOf<>(second.ledger()).size();
    }
}
