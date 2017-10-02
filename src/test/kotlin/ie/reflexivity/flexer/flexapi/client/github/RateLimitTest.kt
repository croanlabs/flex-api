package ie.reflexivity.flexer.flexapi.client.github

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class RateLimitTest {

    @Test
    fun `Given a reamining rate limit of ZERO When checking for Zero Then we expect true to be returned`() {
        val rateLimit = RateLimit(5000, 0, Date().time)

        assertThat(rateLimit.isRemainLimitZero()).isTrue()
    }

    @Test
    fun `Given a remaining rate limit of not ZERO When checking for Zero Then we expect false to be returned`() {
        val rateLimit = RateLimit(rateLimit = 5000, remainingLimit = 1, resetEpochTime = Date().time)

        assertThat(rateLimit.isRemainLimitZero()).isFalse()
    }

    @Test
    fun `Given a reset epoch time in the past When checking if its gone past the reset time Then we expect true to be returned`() {
        val rateLimit = RateLimit(rateLimit = 5000, remainingLimit = 0, resetEpochTime = LocalDateTime.now().minusMinutes(10)
                .atZone(ZoneId.systemDefault()).toEpochSecond())

        assertThat(rateLimit.isPastResetTime()).isTrue()
    }

    @Test
    fun `Given a reset epoch time in the last When checking if its gone past the reset time Then we expect true to be returned`() {
        val rateLimit = RateLimit(rateLimit = 5000, remainingLimit = 0, resetEpochTime = LocalDateTime.now().plusHours(1)
                .atZone(ZoneId.systemDefault()).toEpochSecond())

        assertThat(rateLimit.isPastResetTime()).isFalse()
    }

}
