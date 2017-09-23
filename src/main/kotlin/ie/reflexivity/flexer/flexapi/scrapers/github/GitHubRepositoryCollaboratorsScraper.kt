package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHUser
import org.springframework.stereotype.Service


interface GitHubRepositoryCollaboratorsScraper {
    fun scrape(collaborators: List<GHUser>, repositoryJpa: GitHubRepositoryJpa)
}

@Service
class GitHubRepositoryCollaboratorsScraperImpl(
        val gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository
) : GitHubRepositoryCollaboratorsScraper {

    private val log by logger()

    override fun scrape(collaborators: List<GHUser>, repositoryJpa: GitHubRepositoryJpa) {
        log.debug("About to scrape collaborators for Repo=${repositoryJpa.name}")
        val currentCollaborators = mutableListOf<UserJpa>()
        for (i in 0..collaborators.size - 1) {
            val collaborator = collaborators.elementAt(i)
            currentCollaborators.add(collaborator.toUserJpa())
        }
        repositoryJpa.collaborators.clear()
        repositoryJpa.collaborators.addAll(currentCollaborators)
        gitHubRepositoryJpaRepository.save(repositoryJpa)
    }

}
