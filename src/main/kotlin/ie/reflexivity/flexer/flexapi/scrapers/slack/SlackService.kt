package ie.reflexivity.flexer.flexapi.scrapers.slack

import com.google.gson.JsonObject
import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiPagedResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SlackService {

    /**
     * Checks API calling code.
     */
    @POST("api.test")
    fun test(): Observable<ResponseBody>

    /**
     * Returns list of permissions this app has on a team.
     */
    @POST("apps.permissions.info")
    fun permissions(): Observable<ResponseBody>

    /**
     * Checks authentication & identity.
     */
    @POST("auth.test")
    fun authTest(): Observable<ResponseBody>

    /**
     * Fetches history of apiMessages and events from a channel.
     */
    @POST("channels.history")
    @FormUrlEncoded
    fun channelsHistory(@Field("token") token:String,
                        @Field("channel")channel: String,
                        @Field("count") count:Int,
                        @Field("inclusive") inclusive:Boolean,
                        @Field("latest") latest:String,
                        @Field("oldest") oldest:String,
                        @Field("unreads") unreads:Boolean
                        ): Call<ChannelHistoryApiPagedResponse>

//    @GET("channels.history")
//    fun channelsHistory(@Query("token") token:String,
//                        @Query("channel")channel: String,
//                        @Query("count") count:Int,
//                        @Query("inclusive") inclusive:Boolean,
//                        @Query("latest") latest:String,
//                        @Query("oldest") oldest:String,
//                        @Query("unreads") unreads:Boolean
//    ): Call<ChannelHistoryApiPagedResponse>

    /**
     * Gets information about a channel.
     */
    @POST("channels.info")
    fun channelsInfo(): Observable<ResponseBody>

    /**
     * Lists all channels in a Slack team.
     */
    @POST("channels.list")
    fun channelsList(): Observable<ResponseBody>

    /**
     * Gets information about a private channel. copy
     */
    @POST("groups.info")
    fun groupsInfo(): Observable<ResponseBody>

    /**
     * Exchanges a temporary OAuth code for an API token.
     */
    @POST("oauth.access")
    fun oauthAccess(): Observable<ResponseBody>


    /**
     * Exchanges a temporary OAuth verifier code for a workspace token.
     */
    @POST("oauth.token")
    fun oauthToken(): Observable<ResponseBody>


    /**
     * List of Users
     */
    @POST("users.list")
    fun usersList(): Observable<ResponseBody>


    /**
     * List all User Groups for a team
     */
    @POST("usergroups.list")
    fun usergroupsList(): Observable<ResponseBody>

    /**
     * Gets information about the current team.
     */
    @POST("team.info")
    fun teamInfo(): Observable<ResponseBody>

    /**
     * Gets the access logs for the current team.
     */
    @POST("team.accessLogs")
    fun teamAccessLogs(): Observable<ResponseBody>


    /**
     * Searches for apiMessages and files matching a query.
     */
    @POST("search.all")
    fun searchAll(): Observable<ResponseBody>


    /**
     * Searches for files matching a query.
     */
    @POST("search.files")
    fun searchFiles(): Observable<ResponseBody>

    /**
     * Searches for apiMessages matching a query.
     */
    @POST("search.apiMessages")
    fun searchMessages(): Observable<ResponseBody>

    /**
     * Gets information about a user.
     */
    @POST("users.info")
    fun usersInfo(): Observable<ResponseBody>

    /**
     * Gets information about a user.
     */
    @POST("users.identity")
    fun usersIdentity(): Observable<ResponseBody>
}