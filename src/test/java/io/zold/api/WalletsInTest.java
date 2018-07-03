/**
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
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link WalletsIn}.
 *
 * @author Izbassar Tolegen (t.izbassar@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 */
public final class WalletsInTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void iteratorIsNotYetImplemented() throws IOException {
        this.thrown.expect(UnsupportedOperationException.class);
        this.thrown.expectMessage(
            Matchers.is(
                "iterator() not yet supported"
            )
        );
        new WalletsIn(
            this.folder.newFolder().toPath()
        ).iterator();
    }

    @Test
    public void createIsNotYetImplemented() throws IOException {
        this.thrown.expect(UnsupportedOperationException.class);
        this.thrown.expectMessage(
            Matchers.is(
                "create() not yet supported"
            )
        );
        new WalletsIn(
            this.folder.newFolder().toPath()
        ).create();
    }
}
