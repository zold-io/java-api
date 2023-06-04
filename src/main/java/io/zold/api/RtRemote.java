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

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import java.io.IOException;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Default implementation of {@link Remote}.
 * @since 1.0
 */
public final class RtRemote implements Remote {

    /**
     * Request to Node.
     */
    private final Request req;

    /**
     * Ctor.
     * @param host Remote host
     * @param port Remote port
     */
    public RtRemote(final String host, final int port) {
        this.req = new JdkRequest(
            new UncheckedText(
                new FormattedText("http://%s:%d", host, port)
            ).asString()
        );
    }

    // @todo #38:30min Let us Implement RtRemote.score() here. When implemented,
    //  the unit test RemoteTest.scoreIsNotYetImplemented should be removed and
    //  replaced with an appropriate unit test.
    @Override
    public Score score() {
        throw new UnsupportedOperationException("score() not yet supported");
    }

    // @todo #38:30min Implementation of push() is currently incomplete. We need
    //  to read the entire contents of Wallet and include it in the request body
    //  before fetching. Right now there's no way for us to do that. What I'm
    //  thinking is to add a new method in Wallet interface, Wallet.contents(),
    //  which reads the entire content of the Wallet and returns it. Then pass
    //  that the request body here.
    @Override
    public void push(final Wallet wallet) {
        try {
            this.req.uri().path(
                new UncheckedText(
                    new FormattedText(
                        "wallets/%016x", wallet.id()
                    )
                ).asString()
            ).back().method("PUT")
            .body().set("zold")
            .back()
            .fetch();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    // @todo #38:30min Let us Implement RtRemote.pull() here. When implemented,
    //  the unit test RemoteTest.scoreIsNotYetImplemented should be removed and
    //  replaced with an appropriate unit test.
    @Override
    public Wallet pull(final long id) {
        throw new UnsupportedOperationException("pull() not yet supported");
    }
}
