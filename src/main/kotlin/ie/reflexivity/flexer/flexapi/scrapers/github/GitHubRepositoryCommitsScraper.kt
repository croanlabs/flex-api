package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.repository.GitHubCommitJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.printRateDetails
import ie.reflexivity.flexer.flexapi.extensions.toCommitJpa
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedIterable
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


interface GitHubRepositoryCommitsScraper {
    fun scrape(commitsIterable: PagedIterable<GHCommit>, githubRepository: GitHubRepositoryJpa)
}

@Service
class GitHubRepositoryCommitsScraperImpl(
        val userJpaRepository: UserJpaRepository,
        val gitHubCommitJpaRepository: GitHubCommitJpaRepository,
        private val gitHub: GitHub
) : GitHubRepositoryCommitsScraper {

    private val log by logger()

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private val batchSize: Int = 30

    override fun scrape(commitsIterable: PagedIterable<GHCommit>, githubRepository: GitHubRepositoryJpa) {
        log.debug("About to scrape commits for ${githubRepository.name}")
        gitHub.printRateDetails()
        var count = 0
        try {
            val commitsIterator = commitsIterable.iterator()
            while (commitsIterator.hasNext()) {
                count++
                val commit = commitsIterator.next()
                log.trace("Scraping commit ${commit.shA1}")
                if (commit.author == null || commit.committer == null) {
                    //TODO why? When checking call from API i see commiter, speed issue?
                    log.warn("No author or committer set. ${commit.shA1} ${commit.htmlUrl} ")
                    continue
                }
                createUserIfNeeded(commit.author)
                createUserIfNeeded(commit.committer)
                val commitJpa = commit.toCommitJpa(githubRepository)
                gitHubCommitJpaRepository.save(commitJpa)
                if (count % batchSize == 0) {
                    gitHubCommitJpaRepository.flush()
                }
            }
        } catch (e: Error) {
            //Client Lib throws an error :-(. Happens when repo is empty, 409 returned from API.
            log.error("problem scraping commits for ${githubRepository.name}", e)
        }
        log.debug("Added $count new commits")
    }

    private fun createUserIfNeeded(user: GHUser) =
            userJpaRepository.findByPlatformUserIdAndPlatform(user.login, GIT_HUB) ?:
                    userJpaRepository.saveAndFlush(user.toUserJpa(GIT_HUB))

}
