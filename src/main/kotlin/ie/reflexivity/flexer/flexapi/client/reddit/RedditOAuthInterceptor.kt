package ie.reflexivity.flexer.flexapi.client.reddit

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.POST
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


class RedditOAuthInterceptor(
        val username: String,
        val token: String
) : Interceptor {

    private var redditToken: RedditToken? = null

    override fun intercept(chain: Chain): Response {
        if (redditToken == null || redditToken!!.isTokenExpired()) {
            redditToken = fetchRedditOauthToken()
        }
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header("Authorization",
                "Bearer ${redditToken!!.access_token}")
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

    private fun fetchRedditOauthToken(): RedditToken {
        val basicAuth = Credentials.basic(username, token)
        val fetchToken = RestTemplate()
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, "$basicAuth")
        headers.add(HttpHeaders.USER_AGENT, "This is the flex bot")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
        val bodyMap = LinkedMultiValueMap<String, String>()
        bodyMap.add("grant_type", "client_credentials")
        val request = HttpEntity(bodyMap, headers)
        val response = fetchToken.exchange("https://www.reddit.com/api/v1/access_token",
                POST, request, RedditToken::class.java)
        return response.body
    }
}
