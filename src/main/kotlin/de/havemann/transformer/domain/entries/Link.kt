package de.havemann.transformer.domain.entries

data class Link(val label: String, val url: String) {
    companion object {
        val propertyToGetter = mapOf(
            "label" to Link::label,
            "url" to Link::url
        )
    }
}