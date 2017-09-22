package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.GitHubIssueJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class GitHubIssueJpaRepositoryIT {

    @Inject lateinit var testee: GitHubIssueJpaRepository
    @Inject lateinit var gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `Given a an existing repository When saving a issue Then the issue should be saved`() {
        val (repoJpa, userJpa) = createRepository()

        val result = testee.save(GitHubIssueJpa.testInstance(repoJpa, userJpa))

        assertThat(result).isNotNull()
        assertThat(result.id).isNotNull()
    }

    @Test
    fun `Given a repository with an issue When fetching an issue by gitHubId and repo Id Then the issue should be loaded`() {
        val (repoJpa, userJpa) = createRepository()
        val issueJpa = testee.save(GitHubIssueJpa.testInstance(repoJpa, userJpa))

        val result = testee.fetchIssue(issueJpa.gitHubId, repoJpa.id)
        assertThat(result).isNotNull()
    }

    @Test
    fun `Given a repository with an issue When fetching and the repo Id doesnt exist Then nothing should be returned`() {
        val (repoJpa, userJpa) = createRepository()
        val issueJpa = testee.save(GitHubIssueJpa.testInstance(repoJpa, userJpa))
        val NON_EXISTENT_REPO_ID = repoJpa.id + 1

        val result = testee.fetchIssue(issueJpa.gitHubId, NON_EXISTENT_REPO_ID)

        assertThat(result).isNull()
    }


    private fun createRepository(): Pair<GitHubRepositoryJpa, UserJpa> {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val repoJpa = gitHubRepositoryJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa = projectJpa))
        val userJpa = userJpaRepository.save(UserJpa.testInstance())
        return Pair(repoJpa, userJpa)
    }


}
