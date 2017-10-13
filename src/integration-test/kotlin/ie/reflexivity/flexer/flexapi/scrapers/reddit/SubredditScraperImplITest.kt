package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.FlexIntegrationTest
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
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
class SubredditScraperImplITest {

    @Inject lateinit var subredditScraper: SubredditScraper
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository
    @Inject lateinit var subredditJpaRepository: SubredditJpaRepository

    @Test
    fun `Given a project with a subreddit configured When scraping it Then expect the subredit to be saved`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())

        subredditScraper.scrape(projectJpa.copy(subreddit = "melonproject"))

        assertThat(subredditJpaRepository.findAll().size).isEqualTo(1)
    }


}
