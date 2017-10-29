package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@FlexIntegrationTest // dont use @JpaRepositoryTest here. Need full stack for envers to kick in.
@RunWith(SpringRunner::class)
class GitHubRepositoryJpaRepositoryRevisionsITest {

    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject private lateinit var gitHubRepoRepository: GitHubRepositoryJpaRepository

    @Test
    fun `Given a github repository When saved twice Then it should have an audit trail`() {
        val gitHubRepositoryJpa = createGitHubRepository()
        val expectedAudit1 = gitHubRepoRepository.save(gitHubRepositoryJpa.copy(starGazersCount = 200))
        val expectedAudit2 = gitHubRepoRepository.save(expectedAudit1.copy(starGazersCount = 300))

        val revisions = gitHubRepoRepository.findRevisions(expectedAudit1.id)

        assertThat(revisions.toList().size).isEqualTo(2)
        assertThat(revisions.toList().get(0).entity).isEqualTo(expectedAudit1)
        assertThat(revisions.toList().get(1).entity).isEqualTo(expectedAudit2)
    }

    private fun createGitHubRepository(): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        return GitHubRepositoryJpa.testIntance(projectJpa = projectJpa)
    }
}
