package de.havemann.transformer.domain.entries

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

internal class NavigationEntriesTransformerTest {

    @Test
    fun `transform navigation entries`() {
        val content = this.javaClass.getResource("sample-service-response.json").readText()
        val transformed = NavigationEntriesTransformer(
            NavigationEntriesParser.parse(content).navigationEntries
        ).transform()

        SoftAssertions().apply {
            assertThat(transformed).hasSize(5)
            assertThat(transformed[0].label).isEqualTo("0-6 Monate")
            assertThat(transformed[4].label).isEqualTo("4-5 Jahre")
        }.assertAll()
    }

    @Test
    fun `transform navigation entries with parent`() {
        val content = this.javaClass.getResource("sample-service-response.json").readText()
        val transformed = NavigationEntriesTransformer(
            NavigationEntriesParser.parse(content).navigationEntries,
            "Baby & Kleinkind"
        ).transform()

        SoftAssertions().apply {
            assertThat(transformed).hasSize(3)
            assertThat(transformed[0].label).isEqualTo("0-6 Monate")
            assertThat(transformed[2].label).isEqualTo("13-24 Monate")
        }.assertAll()
    }

    @Test
    fun `transform navigation entries with intermediary node parent`() {
        val content = this.javaClass.getResource("sample-service-response.json").readText()
        val transformed = NavigationEntriesTransformer(
            NavigationEntriesParser.parse(content).navigationEntries,
            "Alter"
        ).transform()

        SoftAssertions().apply {
            assertThat(transformed).hasSize(5)
            assertThat(transformed[0].label).isEqualTo("0-6 Monate")
            assertThat(transformed[2].label).isEqualTo("13-24 Monate")
        }.assertAll()
    }
}