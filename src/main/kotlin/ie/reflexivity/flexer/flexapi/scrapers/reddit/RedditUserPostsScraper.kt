package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditUserApi
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.SubredditPostJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toSubredditPostJpa
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.model.Platform.REDDIT
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface RedditUserPostsScraper {
    fun scrape()
}

@Service
class RedditUserPostsScraperImpl(
        private val userJpaRepository: UserJpaRepository,
        private val subredditUserApi: SubredditUserApi,
        private val subredditPostJpaRepository: SubredditPostJpaRepository,
        private val subredditService: SubredditService
) : RedditUserPostsScraper {

    private val log by logger()

    override fun scrape() {
        log.info("Scraping reddit user posts")
        var hasMoreUserPages = true
        var page = PageRequest(0, 20) as Pageable
        while (hasMoreUserPages) {
            val redditUserJpaPage = userJpaRepository.findByPlatform(platform = REDDIT, pageable = page)
            redditUserJpaPage.forEach { user ->
                scrapeUserPosts(user)
            }
            hasMoreUserPages = redditUserJpaPage.hasNext()
            page = page.next()
            log.debug("Current scraping all users and next page is $page and hasMorePages=$hasMoreUserPages")
        }
        log.info("Finished reddit user posts scraping")
    }

    private fun scrapeUserPosts(user: UserJpa) {
        var hasPages = true
        var redditNextPageTag: String? = null
        var pageCount = 0
        log.debug("About to scrape reddit user posts for ${user.platformUserId}")
        while (hasPages) {
            pageCount++
            val response = subredditUserApi.fetchUserSubmits(user.platformUserId, after = redditNextPageTag).execute()
            val subredditListing = response.body()
            if (subredditListing?.data == null) {
                hasPages = false
                continue
            }
            redditNextPageTag = subredditListing.data.after
            subredditListing.data.children.forEach { postListing ->
                val post = postListing.data
                val existingPost = subredditPostJpaRepository.findByPostId(post.name)
                if (existingPost == null) {
                    val subredditJpa = subredditService.fetchOrCreateSubreddit(post.subreddit, post.subreddit_id)
                    val redditPostJpa = post.toSubredditPostJpa(subreddit = subredditJpa, author = user)
                    subredditPostJpaRepository.save(redditPostJpa)
                }
            }
            log.debug("Scraping pageNo=$pageCount of user subbreddit posts ${user.platformUserId} nextPageTag=$redditNextPageTag")
            if (redditNextPageTag == null) {
                hasPages = false
            }
            subredditPostJpaRepository.flush()
        }
    }

}
