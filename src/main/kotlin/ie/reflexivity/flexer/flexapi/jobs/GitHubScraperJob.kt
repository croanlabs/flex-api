package ie.reflexivity.flexer.flexapi.jobs

import ie.reflexivity.flexer.flexapi.SpringProfiles
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraper
import org.kohsuke.github.GitHub
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.inject.Inject

private const val ONE_HOUR_AND_ONE_MIN = 1000L * 60L * 61

@Component
@Profile("!" + SpringProfiles.TEST_PROFILE)
class GitHubScraperJob {

    @Inject lateinit var gitHubScraper: GitHubScraper
    @Inject lateinit var gitHub: GitHub

    private val log by logger()

    @Scheduled(initialDelay = 1000, fixedRate = ONE_HOUR_AND_ONE_MIN)
    fun startGitHubScraping() {
        log.info("Job triggered to start git hub scraping")
        val rateLimit = gitHub.rateLimit
        log.info("Rate limit is $rateLimit")
        if (rateLimit.remaining > 0) {
            try {
                gitHubScraper.scrape()
            } catch (e: Exception) {
                log.error("Problem occurred with execution run", e)
            }
        }
    }
}
