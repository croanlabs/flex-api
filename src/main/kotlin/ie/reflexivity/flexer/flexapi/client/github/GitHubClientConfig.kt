package ie.reflexivity.flexer.flexapi.client.github

import com.google.gson.GsonBuilder
import ie.reflexivity.flexer.flexapi.client.github.api.GitHubIssueApiService
import ie.reflexivity.flexer.flexapi.client.github.api.GitHubUserApiService
import ie.reflexivity.flexer.flexapi.config.GithubCredentials
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Inject

@Configuration
class GitHubClientConfig {

    @Inject lateinit var gitHubCredentials: GithubCredentials

    @Bean
    fun createGitHubClient(): Retrofit {
        val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, GsonDateTimeConverter())
                .registerTypeAdapter(GitHubState::class.java, GitHubStateConverter())
                .setDateFormat(GIT_HUB_DATE_TIME_FORMAT)
                .create()

        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(
                OAuthTokenInterceptor(gitHubCredentials.token!!))
                .addInterceptor(RateLimitInterceptor())
                .build()
        val gsonFactory = GsonConverterFactory.create(gson)
        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(gsonFactory as Converter.Factory)
                .client(okHttpClient)
                .build()
    }

    @Bean
    fun gitHubIssueService() = createGitHubClient().create(GitHubIssueApiService::class.java)

    @Bean
    fun gitHubUserApiService() = createGitHubClient().create(GitHubUserApiService::class.java)

}
