package ie.reflexivity.flexer.flexapi.client.reddit.api

import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditData
import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditListing
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditPost
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditUserApi {

    @GET("/user/{userId}/about")
    fun fetchUser(@Path("userId") userId: String): Call<SubRedditListing<SubredditUser>>

    @GET("/user/{userId}/submitted")
    fun fetchUserSubmits(@Path("userId") userId: String, @Query("after") after: String? = null)
            : Call<SubRedditListing<SubRedditData<SubredditPost>>>

}
