/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;

/**
 * One copy of a {@link Wallet}.
 *
 * @since 1.0
 */
final class Copy implements Comparable<Copy> {

    /**
     * The wallet.
     */
    private final Wallet wlt;

    /**
     * The remote nodes where the wallet was found.
     */
    private final Iterable<Remote> remotes;

    /**
     * Ctor.
     *
     * @param wallet The wallet
     * @param remotes The remote nodes where the wallet was found
     */
    Copy(final Wallet wallet, final Remote... remotes) {
        this(wallet, new IterableOf<>(remotes));
    }

    /**
     * Ctor.
     *
     * @param wallet The wallet
     * @param remotes The remote nodes where the wallet was found
     */
    Copy(final Wallet wallet, final Iterable<Remote> remotes) {
        this.wlt = wallet;
        this.remotes = remotes;
    }

    @Override
    public int compareTo(final Copy other) {
        return this.score().compareTo(other.score());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Copy
            && this.compareTo((Copy) obj) == 0;
    }

    @Override
    public int hashCode() {
        return new ListOf<>(this.score().suffixes()).size();
    }

    /**
     * Creates new Copy instance with additional remote.
     *
     * @param remote Remote
     * @return Copy Copy
     */
    Copy with(final Remote remote) {
        return new Copy(this.wallet(), new Joined<>(remote, this.remotes));
    }

    /**
     * The wallet.
     *
     * @return The wallet
     */
    Wallet wallet() {
        return this.wlt;
    }

    /**
     * The summary of the score of all the remote nodes.
     *
     * @return The score
     */
    Score score() {
        return new Score.Summed(new Mapped<>(Remote::score, this.remotes));
    }
}

