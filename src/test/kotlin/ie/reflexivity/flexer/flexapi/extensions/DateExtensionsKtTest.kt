package ie.reflexivity.flexer.flexapi.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DateExtensionsKtTest {

    @Test
    fun `Given a todays date When checking if older then a day Then we expect false to be returned`() {
        val result = LocalDate.now().isOlderThanADay()

        assertThat(result).isFalse()
    }

    @Test
    fun `Given a date in future When checking if older then a day Then we expect false to be returned`() {
        val result = LocalDate.now().plusDays(1).isOlderThanADay()

        assertThat(result).isFalse()
    }


    @Test
    fun `Given a day old date When checking if older then a day Then we expect true to be returned`() {
        val result = LocalDate.now().minusDays(1).isOlderThanADay()

        assertThat(result).isTrue()
    }

    @Test
    fun `Given date time of  now When checking if older then a day Then we expect false to be returned`() {
        val result = LocalDateTime.now().isOlderThanADay()

        assertThat(result).isFalse()
    }

    @Test
    fun `Given date time of 25 hours ago When checking if older then a day Then we expect true to be returned`() {
        val result = LocalDateTime.now().minusHours(25).isOlderThanADay()

        assertThat(result).isTrue()
    }

    @Test
    fun `Given a future date time When checking if older then a day Then we expect false to be returned`() {
        val result = LocalDateTime.now().plusDays(1).isOlderThanADay()

        assertThat(result).isFalse()
    }

}
