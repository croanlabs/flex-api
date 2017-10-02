package ie.reflexivity.flexer.flexapi.client.reddit.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface SubredditApiService {

    @GET("/r/{subReddit}")
    fun fetchSubreddit(@Path("subReddit") subReddit: String): Call<String>

}
