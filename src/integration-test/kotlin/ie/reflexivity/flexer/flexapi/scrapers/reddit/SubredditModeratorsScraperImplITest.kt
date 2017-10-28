package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@FlexIntegrationTest
class SubredditModeratorsScraperImplITest {

    @Inject private lateinit var testee: SubredditModeratorsScraper
    @Inject private lateinit var subredditJpaRepository: SubredditJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a subreddit When scraping moderators for a subreddit Then we expect moderators to be added`() {
        val subreddit = createSubreddit()

        testee.scrape("melonproject", subreddit)

        val reloadedSubreddit = subredditJpaRepository.findOne(subreddit.id)
        assertThat(reloadedSubreddit.moderators.size).isGreaterThan(0)
    }

    private fun createSubreddit(): SubredditJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        return subredditJpaRepository.save(SubredditJpa.testInstance(projectJpa))
    }

}
