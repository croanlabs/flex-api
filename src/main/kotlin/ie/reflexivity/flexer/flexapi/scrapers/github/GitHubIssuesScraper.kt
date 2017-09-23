package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubIssueJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubIssueJpa
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.kohsuke.github.GHIssue
import org.kohsuke.github.GHUser
import org.kohsuke.github.PagedIterable
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface GitHubIssuesScraper {
    fun scrape(issues: PagedIterable<GHIssue>, repositoryJpa: GitHubRepositoryJpa)
}

@Service
class GitHubIssuesScraperImpl(
        private val gitHubIssueJpaRepository: GitHubIssueJpaRepository,
        private val userJpaRepository: UserJpaRepository

) : GitHubIssuesScraper {

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private val batchSize: Int = 30

    private val log by logger()

    override fun scrape(issues: PagedIterable<GHIssue>, repositoryJpa: GitHubRepositoryJpa) {
        log.info("Scraping issues for ${repositoryJpa.name}")
        val issuesIterator = issues.iterator()
        var count = 0
        while (issuesIterator.hasNext()) {
            count++
            val issue = issuesIterator.next()
            createUserIfNeeded(issue.user)
            if (issue.closedBy != null) createUserIfNeeded(issue.closedBy)
            val issueJpa = issue.toGitHubIssueJpa(repositoryJpa)
            //Potential bottleneck, single queries vs bulk queries. Lets see.
            val existingIssueJpa = gitHubIssueJpaRepository.fetchIssue(issueJpa.gitHubId, repositoryJpa.id)
            if (existingIssueJpa == null) {
                gitHubIssueJpaRepository.save(issueJpa)
            } else if (existingIssueJpa.isOpen()) {
                gitHubIssueJpaRepository.save(issueJpa.copy(id = existingIssueJpa.id))
            }
            if (count % batchSize == 0) {
                gitHubIssueJpaRepository.flush()
            }
        }
        if (count > 0) gitHubIssueJpaRepository.flush()
        log.info("Scraped $count issues for ${repositoryJpa.name}")
    }

    private fun createUserIfNeeded(user: GHUser) =
            userJpaRepository.findByPlatformUserIdAndPlatform(user.login, GIT_HUB) ?:
                    userJpaRepository.saveAndFlush(user.toUserJpa())
}
