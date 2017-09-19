package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toDate
import ie.reflexivity.flexer.flexapi.extensions.toGitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.web.exceptions.NotFoundException
import org.kohsuke.github.GHPersonSet
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


interface GitHubRepositoryScraper {
    fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa)
}

@Service
class GitHubRepositoryScraperImpl(
        private val gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository,
        private val gitHubRepositoryCollaboratorsScraper: GitHubRepositoryCollaboratorsScraper,
        private val gitHubRepositoryCommitsScraper: GitHubRepositoryCommitsScraper
) : GitHubRepositoryScraper {

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private val batchSize: Int = 30

    private val log by logger()

    override fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa) {
        log.info("About to start scraping repositories for ${projectJpa.projectType} size=${repositories.size}")
        for (i in 0..repositories.size - 1) {
            val ghRepository = repositories.elementAt(i)
            var latestRepoJpa = ghRepository.toGitHubRepositoryJpa(projectJpa)
            val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(latestRepoJpa.gitHubId)
            var lastUpdated = Optional.empty<LocalDateTime>()
            if (existingRepoJpa == null) {
                latestRepoJpa = gitHubRepositoryJpaRepository.save(latestRepoJpa)
            } else {
                lastUpdated = Optional.of(latestRepoJpa.lastModified)
                latestRepoJpa = gitHubRepositoryJpaRepository.save(latestRepoJpa.copy(id = existingRepoJpa.id))
            }
            if (i % batchSize == 0) {
                gitHubRepositoryJpaRepository.flush()
            }
            if (ghRepository.hasPushAccess() && ghRepository.collaborators != null) {
                scrapeCollaborators(latestRepoJpa.gitHubId, ghRepository.collaborators)
            }
            scrapeCommits(ghRepository, latestRepoJpa, lastUpdated)
        }
    }

    private fun scrapeCommits(ghRepository: GHRepository, repositoryJpa: GitHubRepositoryJpa, lastUpdated: Optional<LocalDateTime>) {
        if (lastUpdated.isPresent) {
            val lastDate = lastUpdated.get().toDate()
            log.debug("Only pulling commits since $lastDate")
            val commitsPageable = ghRepository.queryCommits().since(lastDate).list()
            gitHubRepositoryCommitsScraper.scrape(commitsPageable, repositoryJpa)
        } else {
            log.debug("First time initialising commits, will suck in all commits till now")
            val commitsPageable = ghRepository.listCommits()
            gitHubRepositoryCommitsScraper.scrape(commitsPageable, repositoryJpa)
        }
    }

    private fun scrapeCollaborators(id: Int, collaborators: GHPersonSet<GHUser>) {
        val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(id) ?: throw
            NotFoundException("Fail to find repository with github id $id")
        gitHubRepositoryCollaboratorsScraper.scrape(
                collaborators = collaborators.toList(),
                repositoryJpa = existingRepoJpa)
    }

}
