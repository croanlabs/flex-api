package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.model.ProjectType
import ie.reflexivity.flexer.flexapi.model.ProjectType.EOS
import ie.reflexivity.flexer.flexapi.model.ProjectType.ETHERUM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class ProjectJpaRepositoryITest {

    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a project When saving Then the project should be saved`() {
        val etherumProject = createProject()

        val result = projectJpaRepository.save(etherumProject)

        assertThat(result).isNotNull()
        assertThat(result.id).isNotNull()
    }

    @Test
    fun `Given a project that exists When fetching by project type Then the project should be retrieved`() {
        val projectType = EOS
        createAndSaveProject(projectType)

        val result = projectJpaRepository.findOneByProjectType(EOS)

        assertThat(result).isNotNull()
        assertThat(result!!.id).isNotNull()
    }

    @Test
    fun `Given a project with a reddit identifier When fetching it Then the project should be returned`() {
        val subredditId = "subredditId"
        projectJpaRepository.save(createProject().copy(subreddit = subredditId))

        val result = projectJpaRepository.findOneBySubreddit(subredditId)

        assertThat(result).isNotNull()
    }

    @Test
    fun `Given a project without a reddit identifier When fetching it Then null should be returned`() {
        val subredditId = "IDontExist"
        projectJpaRepository.save(createProject())

        val result = projectJpaRepository.findOneBySubreddit(subredditId)

        assertThat(result).isNull()
    }

    private fun createAndSaveProject(projectType: ProjectType): ProjectJpa {
        val project = createProject().copy(projectType = projectType)
        return projectJpaRepository.save(project)
    }

    private fun createProject(): ProjectJpa {
        val projectJpa = ProjectJpa(
                projectType = ETHERUM,
                projectHomePage = "anyHomePage",
                githubUrl = "anyGitHubUrl",
                gitHubOrganisation = "anyOrganisation"
        )
        return projectJpa
    }

}
