package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApi
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service

interface SubredditModeratorsScraper {
    fun scrape(subreddit: String, subredditJpa: SubredditJpa)
}

@Service
class SubredditModeratorsScraperImpl(
        private val subredditApi: SubredditApi,
        private val redditUserService: RedditUserService,
        private val subredditJpaRepository: SubredditJpaRepository
) : SubredditModeratorsScraper {

    private val log by logger()

    override fun scrape(subreddit: String, subredditJpa: SubredditJpa) {
        log.debug("About to scrape moderators for $subreddit")
        val callResponse = subredditApi.fetchSubredditModerators(subReddit = subreddit).execute()
        val moderators = callResponse.body()!!.data.children
        val moderatorsUserJpa = moderators.map { redditUserService.createOrFetchUser(it.name) }.toMutableSet()
        subredditJpa.moderators.clear()
        subredditJpa.moderators.addAll(moderatorsUserJpa)
        subredditJpaRepository.save(subredditJpa)
        log.debug("Scraped for $subreddit ${subredditJpa.moderators.size}")
    }
}
