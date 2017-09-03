package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service


interface GitHubScraper {
    fun scrape()
}

@Service
class GitHubScraperImpl(
        private val gitHubOrganisationScraper: GitHubOrganisationScraper,
        private val gitHubRepositoryScraper: GitHubRepositoryScraper,
        private val projectJpaRepository: ProjectJpaRepository,
        private val gitHub: GitHub
) : GitHubScraper {

    private val log by logger()

    override fun scrape() {
        val projects = projectJpaRepository.findAll()
        log.info("Starting Scraping data for ${projects.size}")
        projects.forEach {
            log.info("Starting Scraping data for ${it.projectType}")
            scrapeOrganisation(it)
        }
    }

    private fun scrapeOrganisation(projectJpa: ProjectJpa) {
        val organisation = gitHub.getOrganization(projectJpa.gitHubOrganisation)
        gitHubOrganisationScraper.scrape(organisation, projectJpa)
        scrapeOrganisationRepositories(organisation, projectJpa)
    }

    private fun scrapeOrganisationRepositories(organisation: GHOrganization, projectJpa: ProjectJpa) {
        val repositories = organisation.repositories.values
        gitHubRepositoryScraper.scrape(repositories, projectJpa)
    }

}


