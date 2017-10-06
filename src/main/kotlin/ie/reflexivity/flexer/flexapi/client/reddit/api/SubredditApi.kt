package ie.reflexivity.flexer.flexapi.client.reddit.api

import ie.reflexivity.flexer.flexapi.client.reddit.RedditModerator
import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditData
import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditList
import ie.reflexivity.flexer.flexapi.client.reddit.SubRedditListing
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditAbout
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditPost
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditApi {

    @GET("/r/{subReddit}")
    fun fetchSubredditPosts(@Path("subReddit") subReddit: String, @Query("after") after: String? = null)
            : Call<SubRedditListing<SubRedditData<SubredditPost>>>

    @GET("/r/{subReddit}/about/moderators")
    fun fetchSubredditModerators(@Path("subReddit") subReddit: String): Call<SubRedditListing<SubRedditList<RedditModerator>>>

    @GET("/r/{subReddit}/about")
    fun fetchSubredditAbout(@Path("subReddit") subReddit: String): Call<SubRedditListing<SubredditAbout>>

}
