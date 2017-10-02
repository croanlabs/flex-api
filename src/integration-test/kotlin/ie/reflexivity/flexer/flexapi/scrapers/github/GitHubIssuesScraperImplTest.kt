package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubIssueJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@FlexIntegrationTest
@RunWith(SpringRunner::class)
class GitHubIssuesScraperImplTest {

    @Inject lateinit var gitHubIssueScraper: GitHubIssuesScraper
    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubRepoJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var gitHubIssueJpaRepository: GitHubIssueJpaRepository

    @Test
    @Ignore("Turned off because it eats up request calls. Useful for testing single unit")
    fun `Given a the golem repository When scraping Then we expect issues to be solved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val anyRepositoryJpa = gitHubRepoJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa))

        gitHubIssueScraper.scrape(ownerName = "golemfactory", repositoryName = "golem", repositoryJpa = anyRepositoryJpa)

        assertThat(gitHubIssueJpaRepository.findAll().size).isGreaterThan(0)
    }

}
