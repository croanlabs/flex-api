package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubCommitJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubIssueJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GitHub
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class GitHubRepositoryScraperImplITest {

    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubRepoJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var testee: GitHubRepositoryScraper
    @Inject lateinit var gitHub: GitHub
    @Inject lateinit var gitHubCommitsJpaRepository: GitHubCommitJpaRepository
    @Inject lateinit var gitHubIssuesJpaRepository: GitHubIssueJpaRepository
    @Inject lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `Given a project When scraping for a repository Then we expect the repository details to be saved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        gitHubRepoJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa))
        val repository = gitHub.getRepository("melonproject/satellite")

        testee.scrape(mutableListOf(repository), projectJpa)

        assertThat(gitHubCommitsJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(gitHubIssuesJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(userJpaRepository.findAll().size).isGreaterThan(0)
    }

    @Test
    fun `Given a project When scraping for second time Then we expect the repository details to be updated`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        gitHubRepoJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa))
        val repository = gitHub.getRepository("melonproject/satellite")

        testee.scrape(mutableListOf(repository), projectJpa)
        testee.scrape(mutableListOf(repository), projectJpa)

        assertThat(gitHubCommitsJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(gitHubIssuesJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(userJpaRepository.findAll().size).isGreaterThan(0)
    }

}
