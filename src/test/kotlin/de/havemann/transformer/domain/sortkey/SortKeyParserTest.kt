package de.havemann.transformer.domain.sortkey

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SortKeyParserTest {

    @Test
    fun `parse with one sort key`() {
        assertThat(SortKeyParser.parse("url"))
            .hasSize(1)
            .first().isEqualTo(SortKey("url", Ordering.ASCENDING))
    }

    @Test
    fun `parse with two sort key and ascending ordering`() {
        assertThat(SortKeyParser.parse("label:asc,url"))
            .hasSize(2)
            .containsExactly(SortKey("label", Ordering.ASCENDING), SortKey("url", Ordering.ASCENDING))
    }

    @Test
    fun `parse with one sort key and descending ordering`() {
        assertThat(SortKeyParser.parse("url:desc"))
            .hasSize(1)
            .first().isEqualTo(SortKey("url", Ordering.DESCENDING))
    }
}