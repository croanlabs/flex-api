package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubCommitJpaRepository
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

interface GitHubRepositoryScraper {
    fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa)
}

@Service
class GitHubRepositoryScraperImpl(
        private val gitHubRepositoryJpaRepository: GitHubRepositoryJpaRepository,
        private val gitHubRepositoryCollaboratorsScraper: GitHubRepositoryCollaboratorsScraper,
        private val gitHubRepositoryCommitsScraper: GitHubRepositoryCommitsScraper,
        private val gitHubCommitJpaRepository: GitHubCommitJpaRepository,
        private val gitHubIssueScraper: GitHubIssuesScraper
) : GitHubRepositoryScraper {

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private val batchSize: Int = 30

    private val log by logger()

    override fun scrape(repositories: MutableCollection<GHRepository>, projectJpa: ProjectJpa) {
        log.info("About to start scraping repositories for ${projectJpa.projectType} size=${repositories.size}")
        for (i in 0..repositories.size - 1) {
            val ghRepository = repositories.elementAt(i)
            val latestRepoJpa = updateRepository(ghRepository, projectJpa)
            if (ghRepository.hasPushAccess() && ghRepository.collaborators != null) {
                scrapeCollaborators(latestRepoJpa.gitHubId, ghRepository.collaborators)
            }
            if (i % batchSize == 0) {
                gitHubRepositoryJpaRepository.flush()
            }
            scrapeCommits(ghRepository, latestRepoJpa)
            gitHubIssueScraper.scrape(ghRepository.ownerName, ghRepository.name, latestRepoJpa)
        }
    }

    private fun updateRepository(ghRepository: GHRepository, projectJpa: ProjectJpa): GitHubRepositoryJpa {
        var latestRepoJpa = ghRepository.toGitHubRepositoryJpa(projectJpa)
        val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(latestRepoJpa.gitHubId)
        if (existingRepoJpa == null) {
            latestRepoJpa = gitHubRepositoryJpaRepository.save(latestRepoJpa)
        } else {
            latestRepoJpa = gitHubRepositoryJpaRepository.save(latestRepoJpa.copy(id = existingRepoJpa.id))
        }
        return latestRepoJpa
    }

    private fun scrapeCommits(ghRepository: GHRepository, repositoryJpa: GitHubRepositoryJpa) {
        val lastCommit = gitHubCommitJpaRepository.findFirstByRepositoryOrderByCommitDateDesc(repositoryJpa)
        if (lastCommit != null) {
            val lastDate = lastCommit.commitDate!!.minusDays(1).toDate()
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
        val existingRepoJpa = gitHubRepositoryJpaRepository.findByGitHubId(id) ?: throw NotFoundException(
                "Fail to find repository with github id $id")
        gitHubRepositoryCollaboratorsScraper.scrape(
                collaborators = collaborators.toList(),
                repositoryJpa = existingRepoJpa)
    }

}
