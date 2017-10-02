package ie.reflexivity.flexer.flexapi.client.reddit

import com.google.gson.GsonBuilder
import ie.reflexivity.flexer.flexapi.client.github.GsonDateTimeConverter
import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApiService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime


fun main(args: Array<String>) {

    val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonDateTimeConverter())
            .create()
    val okHttpClient = OkHttpClient().newBuilder().build()
    val gsonFactory = GsonConverterFactory.create(gson)
    val retroFit = Retrofit.Builder()
            .baseUrl("https://oauth.reddit.com/")
            .addConverterFactory(gsonFactory as Converter.Factory)
            .client(okHttpClient)
            .build()
    val subredditService = retroFit.create(SubredditApiService::class.java)

    val call = subredditService.fetchSubreddit("melonproject")
    val response = call.execute()

    println(response.body())

}
