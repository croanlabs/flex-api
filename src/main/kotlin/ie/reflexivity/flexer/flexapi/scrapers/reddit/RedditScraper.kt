package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch


interface RedditScraper {
    fun scrape()
}

@Service
class RedditScraperImpl(
        private val projectJpaRepository: ProjectJpaRepository,
        private val subredditScraper: SubredditScraper,
        private val redditUserPostsScraper: RedditUserPostsScraper

) : RedditScraper {

    private val log by logger()

    override fun scrape() {
        log.info("About to start reddit scraping")
        val stopWatch = StopWatch()
        stopWatch.start()
        val projects = projectJpaRepository.findAll().filter { it.subreddit != null }
        projects.forEach {
            try {
                subredditScraper.scrape(it)
            } catch (e: Exception) {
                log.error("Problem occurred scraping ${it.subreddit}", e)
            }
        }
        redditUserPostsScraper.scrape()
        stopWatch.stop()
        log.info("Finished reddit Scraping data. ${stopWatch.shortSummary()} TotalSecs = ${stopWatch.totalTimeSeconds}")
    }

}
