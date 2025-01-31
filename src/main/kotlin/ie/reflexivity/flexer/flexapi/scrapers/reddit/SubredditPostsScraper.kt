package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApi
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.repository.SubredditPostJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toSubredditPostJpa
import ie.reflexivity.flexer.flexapi.logger
import org.springframework.stereotype.Service


interface SubredditPostsScraper {
    fun scrape(subreddit: String, subredditJpa: SubredditJpa)
}

@Service
class SubredditPostsScraperImpl(
        private val subredditApi: SubredditApi,
        private val subredditPostJpaRepository: SubredditPostJpaRepository,
        private val redditUserService: RedditUserService
) : SubredditPostsScraper {

    private val log by logger()

    override fun scrape(subreddit: String, subredditJpa: SubredditJpa) {
        log.debug("Scraping subbreddit ${subreddit}")
        var hasPages = true
        var redditNextPageTag: String? = null
        var pageCount = 0
        while (hasPages) {
            pageCount++
            val response = subredditApi.fetchSubredditPosts(subreddit, after = redditNextPageTag).execute()
            val subredditListing = response.body()
            redditNextPageTag = subredditListing!!.data.after
            subredditListing.data.children.forEach { postListing ->
                val post = postListing.data
                val userJpa = redditUserService.createOrFetchUser(post.author)
                val redditPostJpa = post.toSubredditPostJpa(subreddit = subredditJpa, author = userJpa)
                val existingPost = subredditPostJpaRepository.findByPostId(post.name)
                if (existingPost == null) {
                    subredditPostJpaRepository.save(redditPostJpa)
                } else {
                    subredditPostJpaRepository.save(redditPostJpa.copy(id = existingPost.id))
                }
            }
            log.debug("Scraping pageNo=$pageCount of subbreddit ${subreddit} nextPageTag=$redditNextPageTag")
            if (redditNextPageTag == null) {
                hasPages = false
            }
            subredditPostJpaRepository.flush()
        }
    }

}
