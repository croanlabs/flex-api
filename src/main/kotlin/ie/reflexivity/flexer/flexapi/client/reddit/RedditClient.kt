package ie.reflexivity.flexer.flexapi.client.reddit

import com.google.gson.GsonBuilder
import ie.reflexivity.flexer.flexapi.client.github.GsonDateTimeConverter
import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApiService
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.POST
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime


fun main(args: Array<String>) {

    val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonDateTimeConverter())
            .create()
    val gsonFactory = GsonConverterFactory.create(gson)

    val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(RedditOAuthInterceptor())
            .build()

    val retroFit = Retrofit.Builder()
            .baseUrl("https://oauth.reddit.com/")
            .addConverterFactory(gsonFactory as Converter.Factory)
            .client(okHttpClient)
            .build()
    val subredditService = retroFit.create(SubredditApiService::class.java)

    var hasPages = true
    var nextPageTag: String? = null

    var count = 0
    while (hasPages) {
        val call = subredditService.fetchSubreddit(subReddit = "melonproject", after = nextPageTag)
        val response = call.execute()
        val subredditListing = response.body()
        nextPageTag = subredditListing!!.data.after
        subredditListing.data.children.forEach {
            println(it)
            count++
        }
        if (nextPageTag == null) {
            hasPages = false
        }
    }
    println("Reddit has these number of articles $count")
}

class RedditOAuthInterceptor() : Interceptor {

    private var redditToken: RedditToken? = null

    override fun intercept(chain: Chain): Response {
        if (redditToken == null || redditToken!!.isTokenExpired()) {
            redditToken = fetchRedditOauthToken()
            //redditToken = RedditToken(access_token = "cLWZJZtvRyD5PNk3WBkRlV-pyFU", token_type = "bearer", expires_in = 3600, scope = "*")
        }
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header("Authorization",
                "Bearer ${redditToken!!.access_token}")
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

    private fun fetchRedditOauthToken(): RedditToken {
        val basicAuth = Credentials.basic("_3QgWk2uSnqcmw", "jrIEieleMKPsvSnGULQXsOuEVE4")
        val fetchToken = RestTemplate()
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, "$basicAuth")
        headers.add(HttpHeaders.USER_AGENT, "This is the flex bot")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
        val bodyMap = LinkedMultiValueMap<String, String>()
        bodyMap.add("grant_type", "client_credentials")
        val request = HttpEntity(bodyMap, headers)
        val response = fetchToken.exchange("https://www.reddit.com/api/v1/access_token", POST, request, RedditToken::class.java)
        return response.body
    }
}

data class RedditToken(
        val access_token: String,
        val token_type: String,
        val expires_in: Int,
        val scope: String,
        val created: LocalDateTime = LocalDateTime.now()
) {
    fun isTokenExpired() = LocalDateTime.now().isBefore(created.plusSeconds(expires_in.toLong()))
}
