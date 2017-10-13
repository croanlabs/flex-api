package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service


interface RedditScraper {
    fun scrape()
}

@Service
class RedditScraperImpl(
        private val projectJpaRepository: ProjectJpaRepository,
        private val subredditScraper: SubredditScraper
) : RedditScraper {

    private val log by logger()

    override fun scrape() {
        log.info("About to start reddit scraping")
        val projects = projectJpaRepository.findAll().filter { it.subreddit != null }
        projects.forEach {
            subredditScraper.scrape(it)
        }
    }

}
