package de.havemann.transformer.domain.entries

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class NavigationEntriesParser {

    companion object {
        private val objectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)

        fun parse(content: String): NavigationEntries {
            return objectMapper.readValue(content, NavigationEntries::class.java)
        }
    }
}