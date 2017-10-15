package ie.reflexivity.flexer.flexapi.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun `Given a string under the Database max site When shortening Then we expect no change`() {
        val smallString = "smallString"

        val result = smallString.shortenToDBMaxAllowedSize()

        assertThat(result).isEqualTo(smallString)
    }

    @Test
    fun `Given a string over the Database max size When shortening Then we expect it to be the max range`() {
        val stringAppender = StringBuilder()
        for (i in 0..DATBASE_DEFAULT_STRING_SIZE) {
            stringAppender.append("a")
        }
        val overTheLimit = stringAppender.toString()

        val result = overTheLimit.shortenToDBMaxAllowedSize()

        assertThat(result).isEqualTo(overTheLimit.substring(0, DATBASE_DEFAULT_STRING_SIZE))
    }

}
