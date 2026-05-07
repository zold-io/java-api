/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.util.Comparator;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.LengthOf;

/**
 * {@link Remote} nodes that should receive taxes.
 * @since 1.0
 */
public final class TaxBeneficiaries extends IterableEnvelope<Remote> {

    /**
     * Ctor.
     * @param nodes Remote nodes to select from
     * @checkstyle ConstructorsCodeFreeCheck (10 lines)
     */
    public TaxBeneficiaries(final Iterable<Remote> nodes) {
        super(new Sorted<>(
            Comparator.comparing(Remote::score),
            new Filtered<>(
                // @checkstyle MagicNumberCheck (1 line)
                n -> new LengthOf(n.score().suffixes()).value().intValue() >= 16,
                nodes
            )
        ));
    }
}
