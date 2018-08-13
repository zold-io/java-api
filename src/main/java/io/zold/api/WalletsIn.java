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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Random;
import org.cactoos.Scalar;
import org.cactoos.func.IoCheckedFunc;
import org.cactoos.io.Directory;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.SolidScalar;
import org.cactoos.text.FormattedText;
import org.cactoos.text.JoinedText;
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
    private final IoCheckedScalar<Path> path;

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
            new Random()
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
     * @param random Randomizer
     * @param ext Wallets file extension
     */
    public WalletsIn(final Scalar<Path> pth, final String ext,
        final Random random) {
        this.path = new IoCheckedScalar<>(
            new SolidScalar<>(pth)
        );
        this.filter = new IoCheckedFunc<Path, Boolean>(
            (file) -> file.toFile().isFile()
                && FileSystems.getDefault()
                .getPathMatcher(String.format("glob:**.%s", ext))
                .matches(file)
        );
        this.ext = ext;
        this.random = random;
    }

    // @todo #12:30min Create the new wallet in the path with all wallets.
    //  It should contain the correct content according to the
    //  white paper. Also add a the test to validate everything is ok.
    @Override
    public Wallet create() throws IOException {
        final Path wpth = this.path.value().resolve(
            new JoinedText(
                ".",
                Long.toHexString(this.random.nextLong()),
                this.ext
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
    public Iterator<Wallet> iterator() {
        try {
            return new Mapped<Path, Wallet>(
                (pth) -> new Wallet.File(pth),
                new Filtered<>(this.filter, new Directory(this.path.value()))
            ).iterator();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
