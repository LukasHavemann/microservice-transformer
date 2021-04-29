package de.havemann.transformer.domain.sortkey

import java.util.*
import java.util.Comparator.comparing
import kotlin.reflect.KProperty1

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
                .map { sortKey ->
                    val sortingFunction: (t: Entity) -> Value = { sortKeyToProperty[sortKey.key]?.call(it)!! }
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

