package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHOrganization
import org.springframework.stereotype.Service


interface GitHubOrganisationScraper {
    fun scrape(organisation: GHOrganization, it: ProjectJpa)
}

@Service
class GitHubOrganisationScraperImpl(
        private val gitHubOrgJpaRepository: GitHubOrganisationJpaRepository
) : GitHubOrganisationScraper {

    private val log by logger()

    override fun scrape(ghOrganisation: GHOrganization, projectJpa: ProjectJpa) {
        log.info("Starting Scraping data for ${projectJpa.projectType}")
        var latestOrganisationJpa = ghOrganisation.toGitHubOrganisationJpa(projectJpa)
        val existingOrganisationJpa = gitHubOrgJpaRepository.findByGitHubId(ghOrganisation.id)
        if (existingOrganisationJpa != null) {
            latestOrganisationJpa = latestOrganisationJpa.copy(id = existingOrganisationJpa.id)
        }
        gitHubOrgJpaRepository.save(latestOrganisationJpa)
    }

}
