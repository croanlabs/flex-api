package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject


@JpaRepositoryTest
@RunWith(SpringRunner::class)
class GitHubOrganisationJpaRepositoryITest {

    @Inject private lateinit var gitHubOrganisationJpaRepository: GitHubOrganisationJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a new github organisation When saving to an existing project Then the organisation should be saved`() {
        val projectJpa = createProject()
        val gitHubOrganisationJpa = GitHubOrganisationJpa.testInstance(projectJpa = projectJpa)

        val result = gitHubOrganisationJpaRepository.save(gitHubOrganisationJpa)

        assertThat(result).isNotNull()
        assertThat(result.id).isNotNull()
    }

    @Test
    fun `Given a saved organisation When searching by gitHubId that exists Then the github organisation should be returned`() {
        val projectJpa = createProject()
        val gitHubId = 12
        createGitHubOrganisation(projectJpa, gitHubId)

        val result = gitHubOrganisationJpaRepository.findByGitHubId(gitHubId)

        assertThat(result).isNotNull()
        assertThat(result?.gitHubId).isEqualTo(gitHubId)
    }

    private fun createGitHubOrganisation(projectJpa: ProjectJpa, gitHubId: Int) {
        val gitHubOrganisationJpa = GitHubOrganisationJpa.testInstance(projectJpa = projectJpa).copy(gitHubId = gitHubId)
        gitHubOrganisationJpaRepository.save(gitHubOrganisationJpa)
    }

    private fun createProject() = projectJpaRepository.save(ProjectJpa.testInstance())

}
