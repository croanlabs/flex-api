package ie.reflexivity.flexer.flexapi.config

import org.kohsuke.github.GitHub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.inject.Inject

@Configuration
class ScraperConfig {

    @Inject lateinit var gitHubCredentials: GithubCredentials

    @Bean
    fun gitHub() = GitHub.connect(gitHubCredentials.username, gitHubCredentials.token)

}
