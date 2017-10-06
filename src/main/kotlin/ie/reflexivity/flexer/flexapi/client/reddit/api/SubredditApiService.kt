package ie.reflexivity.flexer.flexapi.client.reddit.api

import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditListing
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface SubredditApiService {

    @GET("/r/{subReddit}")
    fun fetchSubreddit(@Path("subReddit") subReddit: String, @Query("after") after: String? = null): Call<SubRedditListing>

    @GET("/r/{subReddit}/about/moderators")
    fun fetchSubredditModerators(@Path("subReddit") subReddit: String): Call<SubRedditListing>


}
