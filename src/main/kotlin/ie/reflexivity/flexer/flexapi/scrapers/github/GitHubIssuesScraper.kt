package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.client.github.GitHubIssue
import ie.reflexivity.flexer.flexapi.client.github.GitHubPage
import ie.reflexivity.flexer.flexapi.client.github.api.GitHubIssueApiService
import ie.reflexivity.flexer.flexapi.client.github.rateLimit
import ie.reflexivity.flexer.flexapi.client.github.toGitHubPage
import ie.reflexivity.flexer.flexapi.db.domain.GitHubIssueJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState.OPEN
import ie.reflexivity.flexer.flexapi.db.repository.GitHubIssueJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toGitHubFormat
import ie.reflexivity.flexer.flexapi.extensions.toGitHubIssueJpa
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service
import retrofit2.Call


interface GitHubIssuesScraper {
    fun scrape(ownerName: String, repositoryName: String, repositoryJpa: GitHubRepositoryJpa)
}

@Service
class GitHubIssuesScraperImpl(
        private val issueApiService: GitHubIssueApiService,
        private val gitHubIssueJpaRepository: GitHubIssueJpaRepository,
        private val gitHubUserService: GitHubUserService
) : GitHubIssuesScraper {

    private val log by logger()

    override fun scrape(ownerName: String, repositoryName: String, repositoryJpa: GitHubRepositoryJpa) {
        val lastIssue = fetchLastIssueSinceLastRun(repositoryJpa)
        var hasMorePages = true
        var pageNumber = 1
        while (hasMorePages) {
            var issuesCall = issueApiService.listIssues(
                    owner = ownerName, repository = repositoryName, page = pageNumber.toString())
            if (lastIssue != null) {
                log.info("Will scrape issues from the oldest open issue which is ${repositoryJpa.name} ${lastIssue}")
                issuesCall = issueApiService.listIssues(
                        owner = ownerName, repository = repositoryName, page = pageNumber.toString(),
                        since = lastIssue.createdOn!!.toGitHubFormat())
            }
            log.debug("Scraping issues for ${repositoryJpa.name} ...pageNumber $pageNumber")
            val gitHubPage = excecuteIssuesCall(issuesCall, repositoryJpa)
            hasMorePages = gitHubPage.hasNext()
            if (hasMorePages) {
                pageNumber = gitHubPage.nextPageNumber
            }
        }
    }

    private fun excecuteIssuesCall(issuesCall: Call<List<GitHubIssue>>, repositoryJpa: GitHubRepositoryJpa): GitHubPage {
        val callResult = issuesCall.execute()
        val issues = callResult.body()!!
        for (issue in issues) {
            val gitHubIssueJpa = createHitHubIssueJpa(issue, repositoryJpa)
            val existingIssueJpa = gitHubIssueJpaRepository.fetchIssue(gitHubIssueJpa.gitHubId, repositoryJpa.id)
            if (existingIssueJpa == null) {
                gitHubIssueJpaRepository.save(gitHubIssueJpa)
            } else if (existingIssueJpa.isOpen()) {
                gitHubIssueJpaRepository.save(gitHubIssueJpa.copy(id = existingIssueJpa.id))
            }
        }
        gitHubIssueJpaRepository.flush()
        log.debug("Rate limit " + callResult.headers().rateLimit())
        return callResult.headers().toGitHubPage()
    }

    private fun createHitHubIssueJpa(gitHubIssue: GitHubIssue, repositoryJpa: GitHubRepositoryJpa): GitHubIssueJpa {
        val createdBy = gitHubUserService.fetchOrCreate(gitHubIssue.user)
        if (gitHubIssue.assignee != null) {
            val closedByUser = gitHubUserService.fetchOrCreate(gitHubIssue.assignee)
            return gitHubIssue.toGitHubIssueJpa(repositoryJpa, createdBy, closedByUser)
        }
        return gitHubIssue.toGitHubIssueJpa(repositoryJpa, createdBy)
    }

    /**
     * Return the last open issue and if none then return the last closed issue.
     */
    private fun fetchLastIssueSinceLastRun(repositoryJpa: GitHubRepositoryJpa): GitHubIssueJpa? {
        val lastOpenIssue = gitHubIssueJpaRepository.findFirstByRepositoryAndStateOrderByCreatedOn(repositoryJpa, OPEN)
        if (lastOpenIssue != null) {
            return lastOpenIssue
        }
        return gitHubIssueJpaRepository.findFirstByRepositoryOrderByCreatedOnDesc(repositoryJpa)
    }
}
