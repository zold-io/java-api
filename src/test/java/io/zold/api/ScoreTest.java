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

import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Sorted;
import org.cactoos.list.ListOf;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

/**
 * Test case for {@link Score}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class ScoreTest {

    @Test
    @SuppressWarnings("unchecked")
    public void fewerSuffixesComeAfter() {
        final Score lowest = new RtScore(
            new IterableOf<>(new TextOf("a"))
        );
        final Score highest = new RtScore(
            new IterableOf<>(new TextOf("b"), new TextOf("c"))
        );
        MatcherAssert.assertThat(
            new Sorted<>(lowest, highest),
            new IsIterableContainingInOrder<>(
                new ListOf<>(new IsEqual<>(highest), new IsEqual<>(lowest))
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sumWithFewerSuffixesComesAfter() {
        final Score lowest = new RtScore(
            new IterableOf<>(new TextOf("a"))
        );
        final Score highone = new RtScore(
            new IterableOf<>(new TextOf("b"), new TextOf("c"))
        );
        final Score hightwo = new RtScore(
            new IterableOf<>(new TextOf("d"), new TextOf("e"))
        );
        final Score sum = new Score.Summed(new IterableOf<>(lowest, highone));
        MatcherAssert.assertThat(
            new Sorted<>(sum, hightwo),
            new IsIterableContainingInOrder<>(
                new ListOf<>(
                    new IsEqual<>(sum),
                    new IsEqual<>(hightwo)
                )
            )
        );
    }
}
