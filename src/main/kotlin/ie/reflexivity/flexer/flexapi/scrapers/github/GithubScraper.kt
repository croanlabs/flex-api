package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service


interface GitHubScraper {
    fun scrape()
}

@Service
class GitHubScraperImpl(
        private val gitHubRepositoryScraper: GitHubRepositoryScraper,
        private val projectJpaRepository: ProjectJpaRepository,
        private val gitHubOrgJpaRepository: GitHubOrganisationJpaRepository,
        private val gitHub: GitHub
) : GitHubScraper {

    private val log by logger()

    override fun scrape() {
        val projects = projectJpaRepository.findAll()
        log.info("Starting Scraping data for ${projects.size}")
        projects.forEach {
            scrape(it)
        }
    }

    private fun scrape(projectJpa: ProjectJpa) {
        log.info("Starting Scraping data for ${projectJpa.projectType}")
        val organisation = gitHub.getOrganization(projectJpa.gitHubOrganisation)
        var latestOrganisationJpa = organisation.toGitHubOrganisationJpa(projectJpa)
        val existingOrganisationJpa = gitHubOrgJpaRepository.findByGitHubId(organisation.id)
        if (existingOrganisationJpa != null) {
            latestOrganisationJpa = latestOrganisationJpa.copy(id = existingOrganisationJpa.id)
        }
        gitHubOrgJpaRepository.save(latestOrganisationJpa)

        val repositories = organisation.repositories.values
        gitHubRepositoryScraper.scrape(repositories, projectJpa)
    }

}


