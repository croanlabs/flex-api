package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubRepositoryJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.GHRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StopWatch
import javax.persistence.EntityManager


interface GitHubRepositoryScraper {
    fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa)
}

@Service
class GitHubRepositoryScraperImpl(
        private val gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository,
        private val entityManager: EntityManager

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
        val stopWatch = StopWatch()
        stopWatch.start()
        for (i in 0..repositories.size - 1) {
            val latestRepoJpa = repositories.elementAt(i).toGitHubRepositoryJpa(projectJpa)
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

        }
        stopWatch.stop()
        log.info("Finished scraping repositories for ${projectJpa.projectType} executionTime=${stopWatch.totalTimeSeconds}")
    }


}
