package de.havemann.transformer.infrastructure

import de.havemann.transformer.Loggable
import de.havemann.transformer.domain.entries.NavigationEntries
import de.havemann.transformer.domain.entries.NavigationEntriesParser
import de.havemann.transformer.domain.service.NavigationEntriesService
import de.havemann.transformer.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class NavigationEntriesWebService : NavigationEntriesService, Loggable {

    @Value("\${de.havemann.lukas.transformer.url}")
    private lateinit var url: String

    @Value("\${de.havemann.lukas.transformer.apitoken}")
    private lateinit var apitoken: String

    private lateinit var client: WebClient

    @PostConstruct
    fun buildClient() {
        client = WebClient.create(url)
    }

    override fun load(): Mono<NavigationEntries> {
        return client.get()
            .header("x-api-key", apitoken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String::class.java)
            .map { NavigationEntriesParser.parse(it) }
            .doOnError { logger().error("error during requesting content", it) }
    }
}