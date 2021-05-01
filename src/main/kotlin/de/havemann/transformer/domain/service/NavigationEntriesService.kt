package de.havemann.transformer.domain.service

import de.havemann.transformer.domain.entries.NavigationEntries
import reactor.core.publisher.Mono

interface NavigationEntriesService {

    fun load(): Mono<NavigationEntries>
}