package ie.reflexivity.flexer.flexapi.client.github.api


import ie.reflexivity.flexer.flexapi.client.github.GitHubIssue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubIssueApiService {

    @GET("/repos/{owner}/{repo}/issues")
    fun listIssues(@Path("owner") owner: String,
                   @Path("repo") repository: String,
                   @Query("since") since: String,
                   @Query("state") state: String = "all",
                   @Query("page") page: String): Call<List<GitHubIssue>>

    @GET("/repos/{owner}/{repo}/issues")
    fun listIssues(@Path("owner") owner: String,
                   @Path("repo") repository: String,
                   @Query("state") state: String = "all",
                   @Query("page") page: String): Call<List<GitHubIssue>>
}
