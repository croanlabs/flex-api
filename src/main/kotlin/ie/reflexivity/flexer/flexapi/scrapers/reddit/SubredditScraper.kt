package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApi
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toSubredditJpa
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service


interface SubredditScraper {
    fun scrape(project: ProjectJpa)
}

@Service
class SubredditScraperImpl(
        private val subredditApi: SubredditApi,
        private val subredditJpaRepository: SubredditJpaRepository,
        private val subredditPostsScraper: SubredditPostsScraper
) : SubredditScraper {

    private val log by logger()

    override fun scrape(project: ProjectJpa) {
        log.info("Scraping reddit about information for ${project.subreddit}")
        val response = subredditApi.fetchSubredditAbout(project.subreddit!!).execute()
        val redditAboutData = response.body()!!.data
        val subredditJpa = redditAboutData.toSubredditJpa(project)
        val existingSubredditJpa = subredditJpaRepository.findByRedditId(subredditJpa.redditId)
        if (existingSubredditJpa == null) {
            val savedSubredditJpa = subredditJpaRepository.save(subredditJpa)
            subredditPostsScraper.scrape(project.subreddit, savedSubredditJpa)
        } else {
            val savedSubredditJpa = subredditJpaRepository.save(subredditJpa.copy(id = existingSubredditJpa.id))
            subredditPostsScraper.scrape(project.subreddit, savedSubredditJpa)
        }
    }
}
