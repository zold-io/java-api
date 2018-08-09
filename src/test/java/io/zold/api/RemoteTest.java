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

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.mock.MkQuery;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link Remote}.
 * @since 1.0
 * @checkstyle JavadocMethod (500 lines)
 */
public final class RemoteTest {

    // @todo #38:30min RtRemote should be able to handle different HTTP response
    //  codes. Here we test HTTP 202 Accepted. Other possible responses as per
    //  the white paper are 400 (if data is corrupt), 402 (if taxes are unpaid),
    //  and 304 (if content is the same as that in the node). Let's handle
    //  those responses and create new tests as well.
    @Test
    public void pushesWallet() throws IOException {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_ACCEPTED)
            ).start()
        ) {
            final long id = 1000L;
            new RtRemote(
                container.home().getHost(), container.home().getPort()
            ).push(new Wallet.Fake(id));
            final MkQuery query = container.take();
            MatcherAssert.assertThat(
                query.uri().getPath(),
                Matchers.containsString(
                    "wallets/00000000000003e8"
                )
            );
            MatcherAssert.assertThat(
                query.body(),
                Matchers.containsString("zold")
            );
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void scoreIsNotYetImplemented() {
        new RtRemote("foo", 1).score();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void pullIsNotYetImplemented() {
        new RtRemote("bar", 1).pull(1);
    }
}
