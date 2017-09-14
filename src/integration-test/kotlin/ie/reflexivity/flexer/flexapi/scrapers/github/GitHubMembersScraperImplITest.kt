package ie.reflexivity.flexer.flexapi.scrapers.github

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toDate
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GHUser
import org.kohsuke.github.PagedIterable
import org.kohsuke.github.PagedIterator
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class GitHubMembersScraperImplITest {

    @Inject lateinit var testee: GitHubMembersScraper
    @Inject lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a project When scraping a users page Then we expect the user to be attached to the project`() {
        val projectJpa = createProjectWithExistingUser()
        val expectedResult = UserJpa.testInstance()
        val pageableUsers = createPageable(expectedResult)

        testee.scrape(pageableUsers, projectJpa)

        val reloadedProjectJpa = projectJpaRepository.findOne(projectJpa.id)
        assertThat(reloadedProjectJpa.users!!.size).isEqualTo(1)
        val result = reloadedProjectJpa.users!!.elementAt(0)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Given a project When scraping a users page which exists already Then we expect the user to be updated`() {
        val existingUser = UserJpa.testInstance()
        val projectJpa = createProjectWithExistingUser(existingUser)
        val updatedGitHubFollowersCount = existingUser.gitHubFollowersCount + 100
        val expectedResult = existingUser.copy(gitHubFollowersCount = updatedGitHubFollowersCount)
        val pageableUsers = createPageable(expectedResult)

        testee.scrape(pageableUsers, projectJpa)

        val reloadedProjectJpa = projectJpaRepository.findOne(projectJpa.id)
        assertThat(reloadedProjectJpa.users!!.size).isEqualTo(1)
        val result = reloadedProjectJpa.users!!.elementAt(0)
        assertThat(result).isEqualTo(expectedResult)
    }

    private fun createPageable(userJpa: UserJpa): PagedIterable<GHUser> {
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
        val pageableIterator = mock<PagedIterable<GHUser>>()
        val pagedIterator = mock<PagedIterator<GHUser>>()
        whenever(pagedIterator.next()).thenReturn(user)
        whenever(pagedIterator.hasNext()).thenReturn(true).thenReturn(false)
        whenever(pageableIterator.iterator()).thenReturn(pagedIterator)
        return pageableIterator
    }

    private fun createProjectWithExistingUser(userJpa: UserJpa) =
            projectJpaRepository.saveAndFlush(ProjectJpa.testInstance(users = mutableSetOf(userJpa)))

    private fun createProjectWithExistingUser() = projectJpaRepository.save(ProjectJpa.testInstance())

}
