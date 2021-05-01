package de.havemann.transformer.domain.entries

import com.fasterxml.jackson.annotation.JsonProperty

data class NavigationEntries(val navigationEntries: List<Entry>)

data class Entry(val type: Type, val label: String, val children: List<Entry>?, val url: String?)

@Suppress("unused")
enum class Type {
    SECTION,
    LINK,
    NODE,

    @JsonProperty("external-link")
    EXTERNAL_LINK
}