package de.havemann.transformer.domain.entries

import java.util.*

class NavigationEntriesTransformer(
    private val entries: List<Entry>,
    private val parentLabel: String? = null,
    private var result: MutableList<Link> = mutableListOf()
) {

    fun transform(): List<Link> {
        if (entries.isEmpty()) return Collections.emptyList()

        entries.forEach { innerTransform(it.label, it.children) }
        return result
    }

    private fun innerTransform(parentLabel: String, children: List<Entry>?) {
        children?.forEach {
            val nodeLabel = it.label
            if (it.type == Type.LINK && isInclude(parentLabel)) {
                val label = if (isInclude(parentLabel)) nodeLabel else "$parentLabel - $nodeLabel"
                result.add(Link(label, it.url ?: throw InvalidLinkException(it)))
            }

            if (it.type == Type.NODE) {
                innerTransform("$parentLabel - $nodeLabel", it.children)
            }
        }
    }

    private fun isInclude(parentLabel: String): Boolean {
        return if (this.parentLabel != null) parentLabel.contains(this.parentLabel) else true
    }
}

class InvalidLinkException(entry: Entry) : IllegalArgumentException(entry.toString())

