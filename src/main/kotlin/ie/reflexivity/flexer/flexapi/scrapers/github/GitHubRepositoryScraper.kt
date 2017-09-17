package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHPersonSet
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager


interface GitHubRepositoryScraper {
    fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa)
}

@Service
class GitHubRepositoryScraperImpl(
        private val gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository,
        private val entityManager: EntityManager,
        private val gitHubRepositoryCollaboratorsScraper: GitHubRepositoryCollaboratorsScraper,
        private val gitHubRepositoryCommitsScraper: GitHubRepositoryCommitsScraper
) : GitHubRepositoryScraper {

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private val batchSize: Int = 30

    private val log by logger()

    /**
     * Batch inserts
     * https://frightanic.com/software-development/jpa-batch-inserts/
     */
    @Transactional
    override fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa) {
        log.info("About to start scraping repositories for ${projectJpa.projectType} size=${repositories.size}")
        for (i in 0..repositories.size - 1) {
            val ghRepository = repositories.elementAt(i)
            val latestRepoJpa = ghRepository.toGitHubRepositoryJpa(projectJpa)
            val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(latestRepoJpa.gitHubId)
            if (existingRepoJpa == null) {
                entityManager.persist(latestRepoJpa)
            } else {
                entityManager.merge(latestRepoJpa.copy(id = existingRepoJpa.id))
            }
            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            if (ghRepository.hasPushAccess() && ghRepository.collaborators != null) {
                scrapeCollaborators(latestRepoJpa.gitHubId, ghRepository.collaborators)
            }
            val savedRepository = gitHubRepositoryJpaRepository.findByGitHubId(latestRepoJpa.gitHubId)
            scrapeCommits(ghRepository, savedRepository)
        }
    }

    private fun scrapeCommits(ghRepository: GHRepository, repositoryJpa: GitHubRepositoryJpa) {
        //TODO use the query commits so we dont suck in all commits every time. see Since!
        //ghRepository.queryCommits()
        val commitsPageable = ghRepository.listCommits()
        gitHubRepositoryCommitsScraper.scrape(commitsPageable, repositoryJpa)
    }

    private fun scrapeCollaborators(id: Int, collaborators: GHPersonSet<GHUser>) {
        val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(id)
        gitHubRepositoryCollaboratorsScraper.scrape(
                collaborators = collaborators.toList(),
                repositoryJpa = existingRepoJpa)
    }

}
