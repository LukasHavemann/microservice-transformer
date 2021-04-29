package de.havemann.transformer.domain.sortkey

import java.util.*

class SortKeyParser(sortKey: String) {
    private val tokenizer = StringTokenizer(sortKey.trim(), ",")

    fun parse(): List<SortKey> {
        return tokenizer.asSequence()
            .map { parse(it as String) }
            .toCollection(mutableListOf())
    }

    private fun parse(keyAndOrdering: String): SortKey {
        val splitted = keyAndOrdering.split(":")
        return when (splitted.size) {
            1 -> SortKey(splitted[0], Ordering.ASCENDING)
            2 -> SortKey(splitted[0], Ordering.detect(splitted[1]))
            else -> throw IllegalArgumentException(keyAndOrdering)
        }
    }
}