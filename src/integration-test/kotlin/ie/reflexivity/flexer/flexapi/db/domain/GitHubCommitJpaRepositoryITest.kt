package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.GitHubCommitJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import javax.inject.Inject

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class GitHubCommitJpaRepositoryITest {

    @Inject lateinit var gitHubCommitJpaRepository: GitHubCommitJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject private lateinit var gitHubRepoRepository: GitHubRepositoryJpaRepository
    @Inject private lateinit var userRepository: UserJpaRepository

    @Test
    fun `Given commits for a repository When fetching the last commit Then we expect the last commit to be returned`() {
        val repositoryJpa = createRepository()
        val user = userRepository.save(UserJpa.testInstance())
        val firstCommit = GitHubCommitJpa.testInstance(repository = repositoryJpa,
                authorAndCommitter = user).copy(lastModified = LocalDateTime.now().minusDays(1))
        val lastCommit = GitHubCommitJpa.testInstance(repository = repositoryJpa,
                authorAndCommitter = user).copy(lastModified = LocalDateTime.now())
        gitHubCommitJpaRepository.save(firstCommit)
        val expectedResult = gitHubCommitJpaRepository.save(lastCommit)

        val result = gitHubCommitJpaRepository.findFirstByRepositoryOrderByLastModifiedDesc(repositoryJpa)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Given repository with no commits When fetching the last commit Then null to be returned`() {
        val repositoryJpa = createRepository()

        val result = gitHubCommitJpaRepository.findFirstByRepositoryOrderByLastModifiedDesc(repositoryJpa)

        assertThat(result).isNull()
    }


    private fun createRepository(): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        return gitHubRepoRepository.save(GitHubRepositoryJpa.testIntance(projectJpa = projectJpa))
    }

}
