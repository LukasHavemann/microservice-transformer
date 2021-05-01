package de.havemann.transformer.adapter

import de.havemann.transformer.Loggable
import de.havemann.transformer.domain.entries.Link
import de.havemann.transformer.domain.entries.NavigationEntriesTransformer
import de.havemann.transformer.domain.service.NavigationEntriesService
import de.havemann.transformer.domain.sortkey.SortKey
import de.havemann.transformer.domain.sortkey.SortKeyParser
import de.havemann.transformer.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
class LinksAdapter(@Autowired val navigationEntriesService: NavigationEntriesService) : Loggable {

    @GetMapping("links")
    fun links(@RequestParam sort: String?, @RequestParam parent: String?): Mono<List<Link>> {
        if (logger().isDebugEnabled) logger().debug("received request with parent=${parent} and sort=${sort}")
        return navigationEntriesService
            .load()
            .map { NavigationEntriesTransformer(it.navigationEntries, parent).transform() }
            .map {
                if (sort != null) {
                    it.sortedWith(SortKey.toComparator(SortKeyParser.parse(sort), Link.propertyToGetter))
                } else {
                    it
                }
            }
            .doOnError { logger().error("error during processing request", it) }
    }
}