package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditPostJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.model.Platform.REDDIT
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class RedditUserPostsScraperImplTest {

    @Inject lateinit var testee: RedditUserPostsScraper
    @Inject lateinit var userJpaRepository: UserJpaRepository
    @Inject lateinit var subredditPostJpaRepository: SubredditPostJpaRepository
    @Inject lateinit var subredditJpaRepository: SubredditJpaRepository

    @Test
    fun `Given a real reddit user When scraping posts Then we expect all their posts and subreddits where their posts were made to be saved`() {
        val realRedditUser = UserJpa.testInstance(platform = REDDIT).copy(platformUserId = "retotrinkler")
        userJpaRepository.save(realRedditUser)

        testee.scrape()

        assertThat(subredditPostJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(subredditJpaRepository.findAll().size).isGreaterThan(1)
    }

}
