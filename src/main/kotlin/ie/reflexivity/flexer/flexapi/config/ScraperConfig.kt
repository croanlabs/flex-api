package ie.reflexivity.flexer.flexapi.config

import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraper
import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraperImpl
import org.kohsuke.github.GitHub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.inject.Inject

@Configuration
class ScraperConfig{

    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubOrganisationRepository : GitHubOrganisationJpaRepository
    @Inject lateinit var gitHubCredentials : GithubCredentials

    @Bean
    fun gitHubScraper(): GitHubScraper = GitHubScraperImpl(projectJpaRepository,gitHubOrganisationRepository,gitHub())

    private fun gitHub() = GitHub.connect(gitHubCredentials.username,gitHubCredentials.token)

}
