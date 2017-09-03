package ie.reflexivity.flexer.flexapi.scrapers.github

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.CleanDatabase
import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toDate
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GitHub
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.junit4.SpringRunner
import java.net.URL
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanDatabase
class GitHubScraperImplITest {

    @Inject lateinit var gitHubOrganisationRepository: GitHubOrganisationJpaRepository
    @Inject lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var gitHubRepositoryScraper: GitHubRepositoryScraperImpl


    private val gitHub: GitHub = mock()
    private val gitHubOrganisation = "gitHubOrg"

    @Test
    fun `Given a project When scraping from github Then we expect to see the githuborganisation saved`() {
        val projectJpa = createAndSaveProject()
        setUpGithubClientResponses(GitHubOrganisationJpa.testInstance(projectJpa))

        testee().scrape()

        val organisations = gitHubOrganisationRepository.findAll()
        assertThat(organisations.size).isEqualTo(1)
        assertThat(organisations[0].projectJpa.id).isEqualTo(projectJpa.id)
    }


    @Test
    fun `Given a project with an existing githuborganisation When scraping from github and blog is different Then we expect the existing github organisation to be updated`() {
        val projectJpa = createAndSaveProject()
        val currentOrganisationJpa = GitHubOrganisationJpa.testInstance(projectJpa = projectJpa)
                .copy(blog = "www.blog.com")
        createAndSaveGitHubOrganisation(currentOrganisationJpa)
        val expectedResult = currentOrganisationJpa.copy(blog = "www.newblog.com")
        val ghOrganisation = createGHOrganisation(expectedResult)
        whenever(gitHub.getOrganization(gitHubOrganisation)).thenReturn(ghOrganisation)

        testee().scrape()

        val organisations = gitHubOrganisationRepository.findAll()
        assertThat(organisations.size).isEqualTo(1)
        assertThat(organisations[0]).isEqualTo(expectedResult)
    }

    private fun setUpGithubClientResponses(gitHubOrganisationJpa: GitHubOrganisationJpa) {
        val organisation = createGHOrganisation(gitHubOrganisationJpa)
        whenever(gitHub.getOrganization(gitHubOrganisation)).thenReturn(organisation)
    }

    private fun testee() = GitHubScraperImpl(gitHubRepositoryScraper, projectJpaRepository, gitHubOrganisationRepository, gitHub)

    private fun createAndSaveProject() =
            projectJpaRepository.save(ProjectJpa.testInstance().copy(gitHubOrganisation = gitHubOrganisation))

    private fun createAndSaveGitHubOrganisation(org: GitHubOrganisationJpa) = gitHubOrganisationRepository.save(org)

    private fun createGHOrganisation(gitHubOrganisationJpa: GitHubOrganisationJpa): GHOrganization {
        val ghOrganisation = mock<GHOrganization>()
        gitHubOrganisationJpa.run {
            whenever(ghOrganisation.blog).thenReturn(blog)
            whenever(ghOrganisation.company).thenReturn(company)
            whenever(ghOrganisation.location).thenReturn(location)
            whenever(ghOrganisation.publicRepoCount).thenReturn(noOfPublicRepos)
            whenever(ghOrganisation.followersCount).thenReturn(noOfFollowers)
            whenever(ghOrganisation.followingCount).thenReturn(following)
            whenever(ghOrganisation.htmlUrl).thenReturn(URL(htmlUrl))
            whenever(ghOrganisation.createdAt).thenReturn(createdAt?.toDate())
            whenever(ghOrganisation.updatedAt).thenReturn(updatedAt?.toDate())
            whenever(ghOrganisation.email).thenReturn(email)
            whenever(ghOrganisation.id).thenReturn(gitHubId)
            whenever(ghOrganisation.publicGistCount).thenReturn(noOfPublicGists)
        }
        return ghOrganisation
    }


}
