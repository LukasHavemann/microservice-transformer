package de.havemann.transformer.adapter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import de.havemann.transformer.domain.entries.Link
import de.havemann.transformer.domain.entries.NavigationEntries
import de.havemann.transformer.domain.entries.NavigationEntriesParser
import de.havemann.transformer.domain.service.NavigationEntriesService
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [LinksAdapter::class])
@Import(LinksAdapterTest.MockWebServiceConfiguration::class)
internal class LinksAdapterTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `base url call smoke test`() {
        webClient.get().uri("/links")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .consumeWith {
                val result = objectMapper.readValue(it.responseBodyContent, object : TypeReference<List<Link>>() {})
                SoftAssertions().apply {
                    assertThat(result).hasSize(190)
                    assertThat(result[0].label).isEqualTo("0-6 Monate")
                    assertThat(result[0].url).isEqualTo("http://www.mytoys.de/0-6-months/")
                }.assertAll()
            }
    }

    @Test
    fun `call with filtering test`() {
        webClient.get().uri("/links?parent=Alter")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .consumeWith {
                val result = objectMapper.readValue(it.responseBodyContent, object : TypeReference<List<Link>>() {})
                SoftAssertions().apply {
                    assertThat(result).hasSize(9)
                    assertThat(result[0].label).isEqualTo("0-6 Monate")
                    assertThat(result[0].url).isEqualTo("http://www.mytoys.de/0-6-months/")
                }.assertAll()
            }
    }

    @Test
    fun `call with filtering and sorting test`() {
        webClient.get().uri("/links?parent=Alter&sort=url:desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .consumeWith {
                val result = objectMapper.readValue(it.responseBodyContent, object : TypeReference<List<Link>>() {})
                SoftAssertions().apply {
                    assertThat(result).hasSize(9)
                    assertThat(result[0].label).isEqualTo("Über 12 Jahre")
                    assertThat(result[0].url).isEqualTo("http://www.mytoys.de/over-156-months/")
                }.assertAll()
            }
    }

    @Test
    fun `call with unknown parent test`() {
        webClient.get().uri("/links?parent=Unknown")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError
    }

    @TestConfiguration
    open class MockWebServiceConfiguration {

        @Primary
        @Bean
        open fun mockService(): NavigationEntriesService {
            val content =
                NavigationEntriesParser.parse(this.javaClass.getResource("service-response.json").readText())
            return object : NavigationEntriesService {
                override fun load(): Mono<NavigationEntries> {
                    return Mono.just(content)
                }
            }
        }
    }
}