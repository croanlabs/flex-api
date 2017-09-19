package ie.reflexivity.flexer.flexapi.scrapers.github

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubCommitJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toDate
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import ie.reflexivity.flexer.flexapi.test.infrastructure.toMockedGHUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GHCommit
import org.kohsuke.github.PagedIterable
import org.kohsuke.github.PagedIterator
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class GitHubRepositoryCommitsScraperImplTest {

    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubRepoJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var gitHubCommitJpaRepository: GitHubCommitJpaRepository
    @Inject lateinit var userJpaRepository: UserJpaRepository
    @Inject lateinit var testee: GitHubRepositoryCommitsScraper

    @Test
    fun `Given a pageable commits When scraping the commits Then the commits should be saved`() {
        val repositoryJpa = createProjectWithRepository()
        val pageableCommits = createPageable(GitHubCommitJpa.testInstance(repositoryJpa))

        testee.scrape(pageableCommits, repositoryJpa)

        val commits = gitHubCommitJpaRepository.findAll()
        assertThat(commits.size).isEqualTo(1)
        assertThat(commits[0].repository.id).isEqualTo(repositoryJpa.id)
    }

    @Test
    fun `Given a pageable commits and user doesnt exist When scraping commits Then user should be created and commit persisted`() {
        val repositoryJpa = createProjectWithRepository()
        val pageableCommits = createPageable(GitHubCommitJpa.testInstance(repositoryJpa))

        testee.scrape(pageableCommits, repositoryJpa)

        val commits = gitHubCommitJpaRepository.findAll()
        assertThat(commits.size).isEqualTo(1)
        assertThat(commits[0].repository.id).isEqualTo(repositoryJpa.id)
    }

    private fun createProjectWithRepository(): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        return gitHubRepoJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa))
    }

    private fun createPageable(commitJpa: GitHubCommitJpa): PagedIterable<GHCommit> {
        val commit = mock<GHCommit>()
        commitJpa.run {
            val mockedAuthor = author.toMockedGHUser()
            whenever(commit.author).thenReturn(mockedAuthor)
            val mockedCommitter = committer.toMockedGHUser()
            whenever(commit.committer).thenReturn(mockedCommitter)
            whenever(commit.authoredDate).thenReturn(authorDate?.toDate())
            whenever(commit.commitDate).thenReturn(commitDate?.toDate())
            whenever(commit.shA1).thenReturn(shaId)
        }
        val pagedIterable = mock<PagedIterable<GHCommit>>()
        val pagedIterator = mock<PagedIterator<GHCommit>>()
        whenever(pagedIterator.next()).thenReturn(commit)
        whenever(pagedIterator.hasNext()).thenReturn(true).thenReturn(false)
        whenever(pagedIterable.iterator()).thenReturn(pagedIterator)
        return pagedIterable
    }

}
