/*
 * SPDX-FileCopyrightText: Copyright (c) 2018-2026 Zerocracy
 * SPDX-License-Identifier: MIT
 */
package io.zold.api;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.cactoos.time.ZonedDateTimeOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link RtTransaction}.
 *
 * @since 0.1
 * @checkstyle LineLengthCheck (500 lines)
 */
final class RtTransactionTest {

    @Test
    void obeysEqualsHashcodeContract() {
        EqualsVerifier.forClass(RtTransaction.class)
            .withNonnullFields("transaction")
            .verify();
    }

    @Test
    void returnsId() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "abcd;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
            ).id(),
            new IsEqual<>(43_981)
        );
    }

    @Test
    void idFormatViolated() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "efgh;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).id()
            ).getMessage(),
            Matchers.startsWith("Invalid ID 'efgh'")
        );
    }

    @Test
    void idStringTooLong() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "abcde;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).id()
            ).getMessage(),
            Matchers.startsWith("Invalid ID 'abcde'")
        );
    }

    @Test
    void idStringTooShort() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "001;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).id()
            ).getMessage(),
            Matchers.startsWith("Invalid ID '001'")
        );
    }

    @Test
    void idNotPresent() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction("").id()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: string is empty")
        );
    }

    @Test
    void parsesAndReturnsIsoFormattedTime() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "003b;2018-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).time(),
            new IsEqual<>(new ZonedDateTimeOf("2018-07-19T21:25:07Z").value())
        );
    }

    @Test
    void invalidTimeFormat() throws IOException {
        Assertions.assertThrows(
            DateTimeParseException.class,
            () -> new RtTransaction(
                "003b;2018-99-19T88:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).time()
        );
    }

    @Test
    void timeNotPresent() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction("003b;").id()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: expected 7 fields, but found 1")
        );
    }

    @Test
    void returnsPositiveAmount() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
            ).amount(),
            new IsEqual<>(10_953_574L)
        );
    }

    @Test
    void returnsNegativeAmount() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
            ).amount(),
            new IsEqual<>(-5_823_642L)
        );
    }

    @Test
    void amountFormatViolated() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;ffffffffffZX2367;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).amount()
            ).getMessage(),
            Matchers.startsWith("Invalid amount 'ffffffffffZX2367'")
        );
    }

    @Test
    void amountStringTooLong() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;00000000000a72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).amount()
            ).getMessage(),
            Matchers.startsWith("Invalid amount '00000000000a72366'")
        );
    }

    @Test
    void amountStringTooShort() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;72366;xksQuJa9;98bb82c81735c4ee;For food;QCuLuVr4..."
                ).amount()
            ).getMessage(),
            Matchers.startsWith("Invalid amount '72366'")
        );
    }

    @Test
    void amountNotPresent() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z"
                ).id()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: expected 7 fields, but found 2")
        );
    }

    @Test
    void returnsPrefix() throws IOException {
        MatcherAssert.assertThat(
            "Returned wrong prefix",
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix(),
            new IsEqual<>("xksQuJa9")
        );
    }

    @Test
    void prefixFormatViolated() {
        Assertions.assertThrows(
            IOException.class,
            () -> new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;|invalidprefix|;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix()
        );
    }

    @Test
    void prefixSizeViolatedLess() {
        Assertions.assertThrows(
            IOException.class,
            () -> new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix()
        );
    }

    @Test
    void prefixSizeViolatedMore() {
        Assertions.assertThrows(
            IOException.class,
            () -> new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4DFF4D;98bb82c81735c4ee; For food;QCuLuVr4..."
            ).prefix()
        );
    }

    @Test
    void prefixNotPresent() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;ffffffffffa72367"
                ).id()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: expected 7 fields, but found 3")
        );
    }

    @Test
    void invalidTransactionString() {
        Assertions.assertThrows(
            IOException.class,
            () -> new RtTransaction(
                "this is a invalid transaction String"
            ).prefix()
        );
    }

    @Test
    void returnsBnf() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;000000C81735c4ee;For food;QCuLuVr4..."
            ).bnf(),
            new IsEqual<>("000000C81735c4ee")
        );
    }

    @Test
    void bnfSizeTooLong() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;000000C81735c4eef;For food;QCuLuVr4..."
                ).bnf()
            ).getMessage(),
            Matchers.startsWith("Invalid bnf string '000000C81735c4eef'")
        );
    }

    @Test
    void bnfSizeTooShort() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;000000C81735c4e;For food;QCuLuVr4..."
                ).bnf()
            ).getMessage(),
            Matchers.startsWith("Invalid bnf string '000000C81735c4e'")
        );
    }

    @Test
    void invalidBnfCharacter() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;000000C81735X4ee;For food;QCuLuVr4..."
                ).bnf()
            ).getMessage(),
            Matchers.startsWith("Invalid bnf string '000000C81735X4ee'")
        );
    }

    @Test
    void bnfNotPresent() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;0000000000a72366;xksQuJa9;"
                ).bnf()
            ).getMessage(),
            Matchers.startsWith(
                "Invalid transaction string: expected 7 fields, but found 4"
            )
        );
    }

    @Test
    void returnsDetails() throws IOException {
        MatcherAssert.assertThat(
            "Returned wrong details",
            new RtTransaction(
                "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;Payment for 3 beers.;QCuLuVr4..."
            ).details(),
            new IsEqual<>("Payment for 3 beers.")
        );
    }

    @Test
    void detailsFormatViolated() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;\\/^&;QCuLuVr4..."
                ).details()
            ).getMessage(),
            Matchers.startsWith("Invalid details string '\\/^&'")
        );
    }

    @Test
    void detailsSizeTooShort() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;ffffffffffa72367;FF4D;98bb82c81735c4ee;;QCuLuVr4..."
                ).details()
            ).getMessage(),
            Matchers.startsWith("Invalid details string ''")
        );
    }

    @Test
    void detailsSizeTooLong() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2017-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;u87d2hova0OhJdYTk5rWxRZeAn6keCBONkERVbcXeUfpZIadF2ncJ5BRNjDjyaPXHaBAmpckf1UZWMq4OKsaVkN9tAmpMm8Fisq3F7E4LWnNFxWdh1LNKlsn7DDvJmB926839C8MZXCfU26AhmC2pgfTUCEkcQHjXjRp4sCXpRRLfYrd134BVjvcq1jFhNDca8JQHutL5PDLagBD80ZLefEOcqoP0YJQrTLnJMQDIhGKHBkPp0NL5IBbRjzmVS4PiW5WV2qHFhUoe98PkMet1OGPsuLIN61ZdeaQwhwEAolqrPDkxELMNz9oF4Zn2uUdubFqvk8OXcPiChpMRlDiyfVMZLgKZaHyL0NqKlhSajFz4KhPNcApOwS2ODxJEJZ9v6HMEWgvVn0MnxrO9Mw6882mf0K7iEEHFUBpxVaeC6MJqQUfZ6YiSNXrulDrVkzcsJEoSLXiPKFpXecmQupJO4f5tv7t59VUFLgQVFIMVHQsyKu7IMNdvW9Akc05T61HE;QCuLuVr4..."
                ).details()
            ).getMessage(),
            Matchers.startsWith(
                "Invalid details string 'u87d2hova0OhJdYTk5rWxRZeAn6keCBONkERVbcXeUfpZIadF2ncJ5BRNjDjyaPXHaBAmpckf1UZWMq4OKsaVkN9tAmpMm8Fisq3F7E4LWnNFxWdh1LNKlsn7DDvJmB926839C8MZXCfU26AhmC2pgfTUCEkcQHjXjRp4sCXpRRLfYrd134BVjvcq1jFhNDca8JQHutL5PDLagBD80ZLefEOcqoP0YJQrTLnJMQDIhGKHBkPp0NL5IBbRjzmVS4PiW5WV2qHFhUoe98PkMet1OGPsuLIN61ZdeaQwhwEAolqrPDkxELMNz9oF4Zn2uUdubFqvk8OXcPiChpMRlDiyfVMZLgKZaHyL0NqKlhSajFz4KhPNcApOwS2ODxJEJZ9v6HMEWgvVn0MnxrO9Mw6882mf0K7iEEHFUBpxVaeC6MJqQUfZ6YiSNXrulDrVkzcsJEoSLXiPKFpXecmQupJO4f5tv7t59VUFLgQVFIMVHQsyKu7IMNdvW9Akc05T61HE'"
            )
        );
    }

    @Test
    void detailsAbsentTransactionString() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2018-99-19T88:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;"
                ).details()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: expected 7 fields, but found 5")
        );
    }

    @Test
    void returnsSignature() throws IOException {
        MatcherAssert.assertThat(
            new RtTransaction(
                "003b;2018-07-19T21:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;For food;WKF3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/JySGxsiy3aJD8L3="
            ).signature(),
            new IsEqual<>("WKF3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/JySGxsiy3aJD8L3=")
        );
    }

    @Test
    void invalidSignatureCharacters() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2018-99-19T88:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;For food;!@#3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/JySGxsiy3aJD8L3W"
                ).signature()
            ).getMessage(),
            Matchers.startsWith("Invalid signature '!@#3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/JySGxsiy3aJD8L3W'")
        );
    }

    @Test
    void invalidSignatureLength() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2018-99-19T88:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;For food;WKF3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/"
                ).signature()
            ).getMessage(),
            Matchers.startsWith(
                "Invalid signature 'WKF3Emi4R2OCbPqUuFSykGD4JtAeWc8WThSYUnmLH2w1RiNSa8KNBGUw56mh0jYjmA1AXRyP/Iktqatmczp+isvh2iBN9hpZgavJ1fFjOgoFmNFe8PT8vg4ZC/vOVgMc807icX7O5i36fi0MEvEv242+2z/Gju3vcb42cZMupDoOEF/CDcEy1Ng7iAFdyLoMja74cMo6H7U0z97e2c2Sa1Eidmkdn+mXNbFsx5RizLtLUfhMtghf697Qu9i1N9lY/Qwk8SvgSviPPWs1cjjK/Fsg1ezfHSWbbHmK8/4qkvgkHqAwVwfD7bWm+1McxzNkU4X5pqE/vW5Tm/K9o7wq4N8u355U+xpgTShPEDN9u6QfdE2O4b/Q6rTzVHMX4j66cLrB8am6K9OQ7LYGShRcqR5L078RyjqHm/wDSzfZWbq8NsjmVm7Dr8NVxJ+0jS2U/r+Fo2+uyjDBn5n/UcqxUGgt0qHaGPoi7kezFHcqeVXp8RLetfXa/TBTj39Gc7aBNBFxkfaM7I/TSpI+xjtjr1cfquut/NiVLMSEVipOIEZ+Sjgf9jLjmpNfRDavz3kGi20TkL4szEvgz8bD1dT6Kf7FKPu7YBMYFoyY12RK7NZcdVr+4yNLFbRtaZYwdhrRLYwKaHli4oUN2CjAOHivVw3Ig8x/'"
            )
        );
    }

    @Test
    void signatureNotPresent() throws IOException {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IOException.class,
                () -> new RtTransaction(
                    "003b;2018-99-19T88:25:07Z;ffffffffffa72367;xksQuJa9;98bb82c81735c4ee;For food;"
                ).signature()
            ).getMessage(),
            Matchers.startsWith("Invalid transaction string: expected 7 fields, but found 6")
        );
    }
}
