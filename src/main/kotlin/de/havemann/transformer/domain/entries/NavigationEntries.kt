package de.havemann.transformer.domain.entries

data class NavigationEntries(val navigationEntries: List<Entry>)

data class Entry(val type: Type, val label: String, val children: List<Entry>?, val url: String?)

enum class Type {
    SECTION, LINK, NODE
}