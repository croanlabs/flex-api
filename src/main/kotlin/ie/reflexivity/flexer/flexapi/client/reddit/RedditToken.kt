package ie.reflexivity.flexer.flexapi.client.reddit

import java.time.LocalDateTime

data class RedditToken(
        val access_token: String,
        val token_type: String,
        val expires_in: Int,
        val scope: String,
        val created: LocalDateTime = LocalDateTime.now()
) {
    fun isTokenExpired() = LocalDateTime.now().isBefore(created.plusSeconds(expires_in.toLong()))
}

