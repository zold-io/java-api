/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Random;
import org.cactoos.Scalar;
import org.cactoos.func.IoCheckedFunc;
import org.cactoos.io.Directory;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Solid;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.UncheckedText;

/**
 * Wallets in path.
 * @since 0.1
 * @checkstyle ClassDataAbstractionCoupling (2 lines)
 */
public final class WalletsIn implements Wallets {

    /**
     * Path containing wallets.
     */
    private final IoChecked<Path> path;

    /**
     * Filter for matching file extensions.
     */
    private final IoCheckedFunc<Path, Boolean> filter;

    /**
     * Wallets file extension.
     */
    private final String ext;

    /**
     * Randomizer.
     */
    private final Random random;

    /**
     * Ctor.
     * @param pth Path with wallets
     */
    public WalletsIn(final Path pth) {
        this(
            () -> pth,
            "z",
            new SecureRandom()
        );
    }

    /**
     * Ctor.
     * @param pth Path with wallets
     * @param random Randomizer
     */
    public WalletsIn(final Path pth, final Random random) {
        this(
            () -> pth,
            "z",
            random
        );
    }

    /**
     * Ctor.
     * @param pth Path with wallets
     * @param ext Wallets file extension
     * @param random Randomizer
     */
    public WalletsIn(final Scalar<Path> pth, final String ext,
        final Random random) {
        this.path = new IoChecked<>(
            new Solid<>(pth)
        );
        this.filter = new IoCheckedFunc<Path, Boolean>(
            file -> file.toFile().isFile()
                && FileSystems.getDefault()
                .getPathMatcher(String.format("glob:**.%s", ext))
                .matches(file)
        );
        this.ext = ext;
        this.random = random;
    }

    @Override
    public Wallet create() throws IOException {
        final Path wpth = this.path.value().resolve(
            new UncheckedText(
                new Joined(
                    ".",
                    Long.toHexString(this.random.nextLong()),
                    this.ext
                )
            ).asString()
        );
        if (wpth.toFile().exists()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        "Wallet in path %s already exists",
                        wpth.toUri().getPath()
                    )
                ).asString()
            );
        }
        Files.createFile(wpth);
        return new Wallet.File(wpth);
    }

    @Override
    public Wallet create(final long id, final String pubkey, final String
        network) throws IOException {
        final Path wpth = this.path.value().resolve(
            new UncheckedText(
                new Joined(
                    ".",
                    Long.toHexString(id),
                    this.ext
                )
            ).asString()
        );
        if (wpth.toFile().exists()) {
            throw new IOException(
                new UncheckedText(
                    new FormattedText(
                        "Wallet in path %s already exists",
                        wpth.toUri().getPath()
                    )
                ).asString()
            );
        }
        // @checkstyle ProhibitLineSeparatorInStringsCheck (10 lines)
        Files.write(
            wpth,
            new UncheckedText(
                new FormattedText(
                    "%s\n1\n%s\n%s\n",
                    network,
                    Long.toHexString(id),
                    pubkey
                )
            ).asString().getBytes(StandardCharsets.UTF_8)
        );
        return new Wallet.File(wpth);
    }

    @Override
    public Iterator<Wallet> iterator() {
        try {
            return new Mapped<Wallet>(
                pth -> new Wallet.File(pth),
                new Filtered<>(this.filter, new Directory(this.path.value()))
            ).iterator();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
