package ie.reflexivity.flexer.flexapi.jobs

import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraper
import org.kohsuke.github.GitHub
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.inject.Inject

private const val FIVE_MINUTES = 1000L * 60L * 5

@Component
class GitHubScraperJob {

    @Inject lateinit var gitHubScraper: GitHubScraper
    @Inject lateinit var gitHub: GitHub

    private val log by logger()

    @Scheduled(fixedRate = FIVE_MINUTES)
    fun startGitHubScraping() {
        log.info("Job triggered to start git hub scraping")
        val rateLimit = gitHub.rateLimit
        log.info("Rate limit is $rateLimit")
        if (rateLimit.remaining > 0) {
            gitHubScraper.scrape()
        }
    }
}
