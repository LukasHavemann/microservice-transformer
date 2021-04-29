package de.havemann.transformer.domain.sortkey

import de.havemann.transformer.domain.entries.Link
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SortKeyTest {
    companion object {
        val CA = Link("c", "a")
        val BC = Link("b", "c")
        val AA = Link("a", "a")
        val CB = Link("c", "b")
        val toBeSorted = listOf(CA, BC, AA, CB)
    }

    @Test
    fun `comparator sort with url ascending`() {
        val list = listOf(SortKey("url", Ordering.ASCENDING))
        assertThat(toBeSorted.sortedWith(SortKey.toComparator(list, Link.propertyToGetter)))
            .containsExactlyElementsOf(listOf(CA, AA, CB, BC))
    }

    @Test
    fun `comparator sort with url descending`() {
        val list = listOf(SortKey("url", Ordering.DESCENDING))
        assertThat(toBeSorted.sortedWith(SortKey.toComparator(list, Link.propertyToGetter)))
            .containsExactlyElementsOf(listOf(BC, CB, CA, AA))
    }

    @Test
    fun `comparator sort with url and label ascending`() {
        val list = listOf(SortKey("url", Ordering.ASCENDING), SortKey("label", Ordering.ASCENDING))
        assertThat(toBeSorted.sortedWith(SortKey.toComparator(list, Link.propertyToGetter)))
            .containsExactlyElementsOf(listOf(AA, CA, CB, BC))
    }
}