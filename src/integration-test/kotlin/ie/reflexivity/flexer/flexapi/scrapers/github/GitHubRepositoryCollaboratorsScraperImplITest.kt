package ie.reflexivity.flexer.flexapi.scrapers.github

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toDate
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import ie.reflexivity.flexer.flexapi.test.infrastructure.testIntance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GHUser
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class GitHubRepositoryCollaboratorsScraperImplITest {

    @Inject lateinit var gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository
    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var testee: GitHubRepositoryCollaboratorsScraper

    @Test
    fun `Given a repository When scraping and there is a collaborator Then we expect the collaborator to be added`() {
        val repositoryJpa = createRepository()
        val expectedResult = UserJpa.testInstance()
        val collaborators = createCollaboratorList(UserJpa.testInstance())

        testee.scrape(collaborators = collaborators, repositoryJpa = repositoryJpa)

        val reloadedRepositoryJpa = gitHubRepositoryJpaRepository.findOne(repositoryJpa.id)
        assertThat(reloadedRepositoryJpa.collaborators.size).isEqualTo(1)
        assertThat(reloadedRepositoryJpa.collaborators.elementAt(0)).isEqualTo(expectedResult)
    }

    @Test
    fun `Given a repository When scraping and there is a collaborator that exists already Then we expect the collaborator to be updated`() {
        val userJpa = UserJpa.testInstance()
        val repositoryJpa = createRepositoryWithCollaborator(userJpa)
        val expectedResult = userJpa.copy(gitHubFollowersCount = userJpa.gitHubFollowersCount + 100)
        val collaborators = createCollaboratorList(expectedResult)

        testee.scrape(collaborators = collaborators, repositoryJpa = repositoryJpa)

        val reloadedRepositoryJpa = gitHubRepositoryJpaRepository.findOne(repositoryJpa.id)
        assertThat(reloadedRepositoryJpa.collaborators.size).isEqualTo(1)
        assertThat(reloadedRepositoryJpa.collaborators.elementAt(0)).isEqualTo(expectedResult)
    }


    private fun createRepositoryWithCollaborator(userJpa: UserJpa): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val repoJpa = gitHubRepositoryJpaRepository.save(
                GitHubRepositoryJpa.testIntance(projectJpa = projectJpa)
                        .copy(collaborators = mutableSetOf(userJpa)))
        return repoJpa
    }


    private fun createRepository(): GitHubRepositoryJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val repoJpa = gitHubRepositoryJpaRepository.save(GitHubRepositoryJpa.testIntance(projectJpa = projectJpa))
        return repoJpa
    }

    private fun createCollaboratorList(userJpa: UserJpa): List<GHUser> {
        val user = mock<GHUser>()
        userJpa.run {
            whenever(user.email).thenReturn(email)
            whenever(user.location).thenReturn(location)
            whenever(user.blog).thenReturn(blog)
            whenever(user.company).thenReturn(company)
            whenever(user.followersCount).thenReturn(gitHubFollowersCount)
            whenever(user.followingCount).thenReturn(gitHubFollowingCount)
            whenever(user.id).thenReturn(platformId!!.toInt())
            whenever(user.createdAt).thenReturn(created?.toDate())
            whenever(user.login).thenReturn(platformUserId)
            whenever(user.name).thenReturn(name)
            whenever(user.id).thenReturn(platformId?.toInt())
        }
        return listOf(user)
    }


}
