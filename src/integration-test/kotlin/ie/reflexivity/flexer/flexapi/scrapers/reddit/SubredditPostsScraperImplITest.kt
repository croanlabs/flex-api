package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditPostJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject


@RunWith(SpringRunner::class)
@FlexIntegrationTest
class SubredditPostsScraperImplITest {

    @Inject lateinit var subredditPostsScraper: SubredditPostsScraper
    @Inject lateinit var subredditJpaRepository: SubredditJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var subredditPostJpaRepository: SubredditPostJpaRepository
    @Inject lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `Given a subreddit When scraping posts for the first time Then we expect posts and users to be saved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val subredditJpa = subredditJpaRepository.save(SubredditJpa.testInstance(projectJpa))

        subredditPostsScraper.scrape("melonproject", subredditJpa)

        assertThat(subredditPostJpaRepository.findAll().size).isGreaterThan(0)
        assertThat(userJpaRepository.findAll().size).isGreaterThan(0)
    }


}
