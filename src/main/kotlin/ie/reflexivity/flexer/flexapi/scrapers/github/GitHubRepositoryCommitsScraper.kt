package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
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
import org.kohsuke.github.GitUser
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
                val commit = commitsIterator.next()
                val exists = gitHubCommitJpaRepository.existsByRepositoryAndShaId(githubRepository, commit.shA1)
                log.trace("Scraping commit ${commit.htmlUrl} . Already exists= ${exists}")
                if (exists == false) {
                    count++
                    val author = createAuthor(commit)
                    val committer = createCommitter(commit)
                    val commitJpa = commit.toCommitJpa(repositoryJpa = githubRepository, author = author, committer = committer)
                    gitHubCommitJpaRepository.save(commitJpa)
                }
                if (count % batchSize == 0) {
                    log.debug("Scrape commit count for ${githubRepository.name} $count")
                    gitHubCommitJpaRepository.flush()
                }
            }
        } catch (e: Error) {
            //Client Lib throws an error :-(. Happens when repo is empty, 409 returned from API.
            //FIXME should mark the repo as invalid on our side -> AD-18
            log.error("problem scraping commits for ${githubRepository.name}", e)
        }
        if (count > 0) gitHubCommitJpaRepository.flush()
        log.debug("Added $count new commits")
    }

    fun createAuthor(commit: GHCommit): UserJpa {
        if (commit.author == null) {
            return createUserIfNeeded(commit.commitShortInfo.author)
        }
        return createUserIfNeeded(commit.author)
    }

    fun createCommitter(commit: GHCommit): UserJpa {
        if (commit.committer == null) {
            return createUserIfNeeded(commit.commitShortInfo.committer)
        }
        return createUserIfNeeded(commit.committer)
    }

    private fun createUserIfNeeded(user: GitUser) =
            userJpaRepository.findByPlatformUserIdAndPlatform(user.email, GIT_HUB) ?:
                    userJpaRepository.saveAndFlush(user.toUserJpa())

    private fun createUserIfNeeded(user: GHUser) =
            userJpaRepository.findByPlatformUserIdAndPlatform(user.login, GIT_HUB) ?:
                    userJpaRepository.saveAndFlush(user.toUserJpa())

}
