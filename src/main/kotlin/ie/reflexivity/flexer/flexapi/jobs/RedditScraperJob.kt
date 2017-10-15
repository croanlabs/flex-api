package ie.reflexivity.flexer.flexapi.jobs

import ie.reflexivity.flexer.flexapi.SpringProfiles
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.scrapers.reddit.RedditScraper
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
@Profile(SpringProfiles.CLOUD)
class RedditScraperJob {

    @Inject lateinit var redditScraper: RedditScraper

    private val log by logger()

    @Scheduled(cron = "0 0 22 * * ?")
    fun startRedditScraping() {
        log.info("About to start reddit scraping")
        try {
            redditScraper.scrape()
        } catch (e: Exception) {
            log.error("Problem scraping for reddit", e)
        }
    }
}
