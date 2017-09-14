package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRateLimit
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedIterable
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch

interface GitHubScraper {
    fun scrape()
}

@Service
class GitHubScraperImpl(
        private val gitHubMembersScraper: GitHubMembersScraper,
        private val gitHubOrganisationScraper: GitHubOrganisationScraper,
        private val gitHubRepositoryScraper: GitHubRepositoryScraper,
        private val projectJpaRepository: ProjectJpaRepository,
        private val gitHub: GitHub
) : GitHubScraper {

    private val log by logger()

    override fun scrape() {
        val projects = projectJpaRepository.findAll()
        var currentRate = gitHub.rateLimit
        log.info("Starting Scraping data for ${projects.size}. GithubRate settings ${currentRate}")
        val stopWatch = StopWatch()
        stopWatch.start()
        projects.forEach {
            log.info("Starting Scraping data for ${it.projectType}")
            if (it.isGitOrganistation()) {
                scrapeOrganisation(it)
            } else if (it.isGitRepository()) {
                scrapeRepository(it)
            }
            currentRate = printStatistics(currentRate, it)
        }
        stopWatch.stop()
        log.info("Finished Github Scraping data. ${stopWatch.shortSummary()}")
    }

    private fun scrapeRepository(projectJpa: ProjectJpa) {
        val repository = gitHub.getRepository(projectJpa.gitHubRepository)
        gitHubRepositoryScraper.scrape(mutableListOf(repository), projectJpa)
    }

    private fun scrapeOrganisation(projectJpa: ProjectJpa) {
        val organisation = gitHub.getOrganization(projectJpa.gitHubOrganisation)
        gitHubOrganisationScraper.scrape(organisation, projectJpa)
        scrapeOrganisationRepositories(organisation, projectJpa)
        scrapeMembers(organisation.listMembers(), projectJpa)
        scrapeMembers(organisation.listPublicMembers(), projectJpa)
    }

    private fun scrapeMembers(listMembers: PagedIterable<GHUser>, projectJpa: ProjectJpa) {
        gitHubMembersScraper.scrape(listMembers, projectJpa)
    }

    private fun scrapeOrganisationRepositories(organisation: GHOrganization, projectJpa: ProjectJpa) {
        val repositories = organisation.repositories.values
        gitHubRepositoryScraper.scrape(repositories, projectJpa)
    }

    private fun printStatistics(currentRate: GHRateLimit, it: ProjectJpa): GHRateLimit {
        val rate = gitHub.rateLimit
        val exhaustedRate = currentRate.remaining - rate.remaining
        log.info("Github rate limit now is ${rate.remaining}. Used calls for ${it.projectType} was ${exhaustedRate}")
        return rate
    }

}


