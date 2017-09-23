package ie.reflexivity.flexer.flexapi.jobs

import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class GitHubScraperJob {

    @Inject lateinit var gitHubScraper: GitHubScraper

    private val log by logger()

    @Scheduled(cron = "0 0 * * * *")
    fun startGitHubScraping() {
        log.info("Job triggered to start git hub scraping")
        gitHubScraper.scrape()
    }
}
