package de.havemann.transformer.domain.entries

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

class NavigationEntriesParsingTest {

    @Test
    fun `parse sample json`() {
        val content = this.javaClass.getResource("sample-service-response.json").readText()
        val parsed = NavigationEntriesParser.parse(content)

        SoftAssertions().apply {
            assertThat(parsed.navigationEntries).hasSize(1)
            assertThat(parsed.navigationEntries[0].type).isEqualTo(Type.SECTION)
            assertThat(parsed.navigationEntries[0].children?.get(0)?.children).hasSize(2)
        }.assertAll()
    }
}