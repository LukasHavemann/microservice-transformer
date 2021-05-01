package de.havemann.transformer.adapter

import de.havemann.transformer.base.Loggable
import de.havemann.transformer.base.logger
import de.havemann.transformer.domain.entries.Link
import de.havemann.transformer.domain.entries.NavigationEntriesTransformer
import de.havemann.transformer.domain.service.NavigationEntriesService
import de.havemann.transformer.domain.sortkey.SortKey
import de.havemann.transformer.domain.sortkey.SortKeyParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
class LinksAdapter(@Autowired val navigationEntriesService: NavigationEntriesService) : Loggable {

    @GetMapping("links")
    fun links(
        @RequestParam("sort") sortCriteria: String?,
        @RequestParam parent: String?
    ): Mono<ResponseEntity<List<Link>>> {
        if (logger().isDebugEnabled) logger().debug("received request with parent=${parent} and sort=${sortCriteria}")

        val sorter: Comparator<Link>? = sorter(sortCriteria)
        return navigationEntriesService
            .load()
            .map { NavigationEntriesTransformer(it.navigationEntries, parent).transform() }
            .doOnNext { if (it.isEmpty()) throw EmptyNavigationEntriesException(parent) }
            .map { if (sorter != null) it.sortedWith(sorter) else it }
            .map { ResponseEntity(it, HttpStatus.OK) }
            .doOnError { logger().error("error during processing request", it) }
    }

    private fun sorter(sortCriteria: String?): Comparator<Link>? {
        if (sortCriteria == null) {
            return null
        }
        return SortKey.toComparator(SortKeyParser.parse(sortCriteria), Link.propertyToGetter)
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class EmptyNavigationEntriesException(parent: String?) : Throwable(parent ?: "no parent provided")
