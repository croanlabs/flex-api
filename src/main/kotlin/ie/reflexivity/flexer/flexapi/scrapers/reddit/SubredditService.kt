package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditApi
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toSubredditJpa
import org.springframework.stereotype.Service

@Service
class SubredditService(
        private val subredditApi: SubredditApi,
        private val subredditJpaRepository: SubredditJpaRepository,
        private val projectJpaRepository: ProjectJpaRepository
) {

    fun fetchOrCreateSubreddit(subreddit: String, subreddit_id: String): SubredditJpa {
        val subreditJpa = subredditJpaRepository.findByRedditId(subreddit_id)
        if (subreditJpa != null) return subreditJpa

        val subredditResponse = subredditApi.fetchSubredditAbout(subreddit).execute()
        if (subredditResponse.isSuccessful) {
            val subredditAbout = subredditResponse.body()!!.data
            val projectJpa = projectJpaRepository.findOneBySubreddit(subreddit)
            val subredditJpa = subredditAbout.toSubredditJpa(projectJpa)
            return subredditJpaRepository.save(subredditJpa)
        }
        throw RedditException("Failed to create subreddit $subreddit")
    }

}
