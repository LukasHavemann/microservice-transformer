package de.havemann.transformer.domain.sortkey

import java.util.*
import java.util.Comparator.comparing
import kotlin.reflect.KProperty1

/**
 * Sorting is determined through the use of the ‘sort’ query string parameter. The value of this parameter is a
 * comma-separated list of sort keys. Sort directions can optionally be appended to each sort key, separated by the ‘:’
 * character.
 *
 * The supported sort directions are either ‘asc’ for ascending or ‘desc’ for descending.
 *
 * The caller may (but is not required to) specify a sort direction for each key. If a sort direction is not specified
 * for a key, then a default is set by the server.
 */
data class SortKey(val key: String, val ordering: Ordering) {
    companion object {

        /**
         * Transforms a list of {@link SortKey} to a {@link Comparator}
         */
        fun <Entity, Value : Comparable<Value>> toComparator(
            sortKeys: List<SortKey>,
            sortKeyToProperty: Map<String, KProperty1<Entity, Value?>>
        ): Comparator<Entity> {
            return sortKeys
                .stream()
                .filter { sortKeyToProperty[it.key] != null }
                .map { sortKey ->
                    val property = sortKeyToProperty[sortKey.key]
                    val sortingFunction: (t: Entity) -> Value = { property?.call(it)!! }
                    sortKey.ordering.adjust(comparing(sortingFunction))
                }
                .reduce { a, b -> a.thenComparing(b) }
                .orElseThrow { IllegalArgumentException("list shouldn't be empty") }
        }
    }
}

enum class Ordering(val key: String) {
    ASCENDING("asc"),
    DESCENDING("desc");

    /**
     * reverses the given comparator if ordering is descending
     */
    fun <T> adjust(comparator: Comparator<T>): Comparator<T> {
        return if (this == DESCENDING) comparator.reversed() else comparator
    }

    companion object {
        /**
         * determines Ordering form given string
         */
        fun detect(token: String): Ordering {
            return values().asSequence()
                .filter { it.key == token }
                .firstOrNull()
                ?: throw IllegalArgumentException(token)
        }
    }
}

