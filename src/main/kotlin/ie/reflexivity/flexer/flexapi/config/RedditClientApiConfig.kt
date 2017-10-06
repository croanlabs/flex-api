package ie.reflexivity.flexer.flexapi.config

import com.google.gson.GsonBuilder
import ie.reflexivity.flexer.flexapi.client.github.GsonDateTimeConverter
import ie.reflexivity.flexer.flexapi.client.reddit.RedditOAuthInterceptor
import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApi
import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditUserApi
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

@Configuration
class RedditClientApiConfig {

    @Value("\${reddit.username}")
    var username: String? = null

    @Value("\${reddit.token}")
    var token: String? = null


    @Bean
    fun redditRetroFit(): Retrofit {
        val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, GsonDateTimeConverter())
                .create()
        val gsonFactory = GsonConverterFactory.create(gson)

        val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(RedditOAuthInterceptor(username = username!!, token = token!!))
                .build()

        return Retrofit.Builder()
                .baseUrl("https://oauth.reddit.com/")
                .addConverterFactory(gsonFactory as Converter.Factory)
                .client(okHttpClient)
                .build()
    }

    @Bean
    fun subredditApi() = redditRetroFit().create(SubredditApi::class.java)

    @Bean
    fun subredditUserApi() = redditRetroFit().create(SubredditUserApi::class.java)

}
