package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject


@JpaRepositoryTest
@RunWith(SpringRunner::class)
class GitHubRepositoryJpaRepositoryITest {

    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject private lateinit var gitHubRepoRepository: GitHubRepositoryJpaRepository

    @Test
    fun `Given a new github repository When saving Then it should be saved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val gitHubRepositoryJpa = GitHubRepositoryJpa.testIntance(projectJpa = projectJpa)

        val result = gitHubRepoRepository.save(gitHubRepositoryJpa)

        assertThat(result).isNotNull()
        assertThat(result.id).isNotNull()
    }

    @Test
    fun `Given an existing gitHubRepoJpa When fetching by gitHubId Then the corresponding repoJpa should be returned`() {
        val existingRepositoryJpa = createGitHubRepository()

        val result = gitHubRepoRepository.findByGitHubId(existingRepositoryJpa.gitHubId)

        assertThat(result).isNotNull()
        assertThat(result!!.id).isNotNull()
    }

    private fun createGitHubRepository(): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val gitHubRepositoryJpa = GitHubRepositoryJpa.testIntance(projectJpa = projectJpa)
        return gitHubRepoRepository.save(gitHubRepositoryJpa)
    }


}
