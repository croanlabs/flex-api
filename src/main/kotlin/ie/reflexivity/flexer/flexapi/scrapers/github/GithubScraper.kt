package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GitHub


interface GitHubScraper {
    fun scrape()
}

class GitHubScraperImpl(
        private val projectJpaRepository: ProjectJpaRepository,
        private val gitHubOrgJpaRepository: GitHubOrganisationJpaRepository,
        private val gitHub : GitHub
): GitHubScraper {

    private val log by logger()

    override fun scrape() {
        val projects = projectJpaRepository.findAll()
        log.info("Starting Scraping data for ${projects.size}")
        projects.forEach {
            scrape(it)
        }
    }

    private fun scrape(projectJpa: ProjectJpa){
        log.info("Starting Scraping data for ${projectJpa.projectType}")
        //val github = GitHub.connect(GitHubCredentials.USER, GitHubCredentials.TOKEN)
        val organisation = gitHub.getOrganization(projectJpa.gitHubOrganisation)
        val gitHubOrgJpa = gitHubOrgJpaRepository.findByGitHubId(organisation.id)
        if(gitHubOrgJpa == null){
            gitHubOrgJpaRepository.save(organisation.toGitHubOrganisationJpa(projectJpa))
        }else{
            val organisationJpa = organisation.toGitHubOrganisationJpa((projectJpa)).copy(id = gitHubOrgJpa.id)
            gitHubOrgJpaRepository.save(organisationJpa)
        }
        scrapeRepositories(organisation,projectJpa)

    }

    private fun scrapeRepositories(organisation: GHOrganization, projectJpa: ProjectJpa){
        val repositories = organisation.repositories.values
        repositories.forEach {

        }
    }

}


