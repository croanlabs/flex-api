package ie.reflexivity.flexer.flexapi.config

import ie.reflexivity.flexer.flexapi.scrapers.github.AbuseLimitErrorHandler
import ie.reflexivity.flexer.flexapi.scrapers.github.RateLimitErrorHandler
import org.kohsuke.github.GitHubBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.inject.Inject

@Configuration
class ScraperConfig {

    @Inject lateinit var gitHubCredentials: GithubCredentials

    @Bean
    fun gitHub() = GitHubBuilder().withOAuthToken(gitHubCredentials.token, gitHubCredentials.username)
            .withAbuseLimitHandler(AbuseLimitErrorHandler())
            .withRateLimitHandler(RateLimitErrorHandler())
            .build()


//    @Bean
//    fun gitHub2() = GitHub.connect(gitHubCredentials.username, gitHubCredentials.token)


}
