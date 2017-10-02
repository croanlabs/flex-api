package ie.reflexivity.flexer.flexapi.client.github.api

import ie.reflexivity.flexer.flexapi.client.github.GitHubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubUserApiService {

    @GET("/users/{userId}")
    fun fetchUser(@Path("userId") userId: String): Call<GitHubUser>
}
