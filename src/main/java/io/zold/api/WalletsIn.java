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

import java.nio.file.Path;
import java.util.Iterator;
import org.cactoos.Scalar;
import org.cactoos.scalar.IoCheckedScalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.SyncScalar;

/**
 * Wallets in path.
 *
 * @since 0.1
 */
@SuppressWarnings({"PMD.SingularField", "PMD.UnusedPrivateField"})
public final class WalletsIn implements Wallets {

    /**
     * Path containing wallets.
     */
    private final IoCheckedScalar<Path> path;

    /**
     * Ctor.
     * @param pth Path with wallets
     */
    public WalletsIn(final Path pth) {
        this(
            () -> pth
        );
    }

    /**
     * Ctor.
     * @param pth Path with wallets
     */
    public WalletsIn(final Scalar<Path> pth) {
        this.path = new IoCheckedScalar<>(
            new SyncScalar<>(
                new StickyScalar<>(pth)
            )
        );
    }

    // @todo #4:30min Return the new instance of the Wallet, that will
    //  be created in the path with all wallets. Should be taken care of
    //  after Wallet interface will have implementations. Cover with tests and
    //  remove irrelevant test case.
    @Override
    public Wallet create() {
        throw new UnsupportedOperationException("create() not yet supported");
    }

    // @todo #4:30min Read instance of the Wallet from file and put it
    //  to the result. Should be taken care of after Wallet interface will have
    //  necessary implementations. Cover with tests and remove irrelevant test
    //  case.
    @Override
    public Iterator<Wallet> iterator() {
        throw new UnsupportedOperationException("iterator() not yet supported");
    }
}
