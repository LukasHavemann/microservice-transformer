package de.havemann.transformer.domain.sortkey

import java.util.*

class SortKeyParser {
    companion object {
        fun parse(sortKey: String): List<SortKey> {
            return StringTokenizer(sortKey.trim(), ",").asSequence()
                .map { parseOneKey(it as String) }
                .toCollection(mutableListOf())
        }

        private fun parseOneKey(keyAndOrdering: String): SortKey {
            val splitted = keyAndOrdering.split(":")
            return when (splitted.size) {
                1 -> SortKey(splitted[0], Ordering.ASCENDING)
                2 -> SortKey(splitted[0], Ordering.detect(splitted[1]))
                else -> throw IllegalArgumentException(keyAndOrdering)
            }
        }
    }
}