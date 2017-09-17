package ie.reflexivity.flexer.flexapi.scrapers.github

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.CleanDatabase
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GHRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanDatabase
class GitHubRepositoryScraperImplITest {

    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubRepoJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var testee: GitHubRepositoryScraper

    private val NUMBER_OF_REPOS = 1_000
    private val MODULUS = 25

    @Test
    fun `Given a GitHubScraper When passed GHRepositories Then repositories should be saved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val ghRepositories = createGHRepostories()

        testee.scrape(repositories = ghRepositories, projectJpa = projectJpa)

        val size = gitHubRepoJpaRepository.findAll().count()
        assertThat(size).isEqualTo(ghRepositories.size)
    }

    @Test
    fun `Given GHRepositories which some are already saved When scraping Then we expect repositories to be saved or updated`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val ghRepositories = createAndSaveRandomlyGHRepostories(projectJpa)

        testee.scrape(repositories = ghRepositories, projectJpa = projectJpa)

        val size = gitHubRepoJpaRepository.findAll().count()
        assertThat(size).isEqualTo(ghRepositories.size)
    }

    private fun createAndSaveRandomlyGHRepostories(projectJpa: ProjectJpa): MutableList<GHRepository> {
        val ghRepositories = createGHRepostories()
        for (i in 0..ghRepositories.size - 1) {
            if (i % MODULUS == 0) {
                val gitHubRepoJpa = ghRepositories[i].toGitHubRepositoryJpa(projectJpa).copy(name = "UpdatedName")
                gitHubRepoJpaRepository.save(gitHubRepoJpa)
            }
        }
        return ghRepositories
    }

    private fun createGHRepostories(): MutableList<GHRepository> {
        val ghRepositories = mutableListOf<GHRepository>()
        for (i in 1..NUMBER_OF_REPOS) {
            val repo = createGHRepository(GitHubRepositoryJpa.testIntance().copy(gitHubId = i))
            ghRepositories.add(repo)
        }
        return ghRepositories
    }

    private fun createGHRepository(gitHubRepostoryJpa: GitHubRepositoryJpa): GHRepository {
        val ghRepository = mock<GHRepository>()
        gitHubRepostoryJpa.run {
            whenever(ghRepository.id).thenReturn(gitHubId)
            whenever(ghRepository.language).thenReturn(language)
            whenever(ghRepository.ownerName).thenReturn(ownerName)
            whenever(ghRepository.stargazersCount).thenReturn(starGazersCount)
            whenever(ghRepository.watchers).thenReturn(watchersCount)
            whenever(ghRepository.forks).thenReturn(forksCount)
            whenever(ghRepository.openIssueCount).thenReturn(openIssuesCount)
            whenever(ghRepository.name).thenReturn(name)
        }
        return ghRepository
    }

}
