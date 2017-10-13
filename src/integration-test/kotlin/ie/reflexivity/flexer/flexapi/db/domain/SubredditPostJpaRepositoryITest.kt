package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
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

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class SubredditPostJpaRepositoryITest {

    @Inject lateinit var testee: SubredditPostJpaRepository
    @Inject lateinit var userJpaRepository: UserJpaRepository
    @Inject lateinit var subredditJpaRepository: SubredditJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a subreddit post When saving it Then expect it to be retrieveable from the DB`() {
        val userJpa = userJpaRepository.save(UserJpa.testInstance(platform = REDDIT))
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val subredditJpa = subredditJpaRepository.save(SubredditJpa.testInstance(projectJpa))

        val result = testee.save(SubredditPostJpa.testInstance(author = userJpa, subredditJpa = subredditJpa))

        assertThat(result).isEqualTo(testee.findOne(result.id))
    }

    @Test
    fun `Given a saved subreddit post When fetching by reddit id Then expect it to be returned from the DB`() {
        val userJpa = userJpaRepository.saveAndFlush(UserJpa.testInstance(platform = REDDIT))
        val projectJpa = projectJpaRepository.saveAndFlush(ProjectJpa.testInstance())
        val subredditJpa = subredditJpaRepository.saveAndFlush(SubredditJpa.testInstance(projectJpa))
        val expectedResult = testee.saveAndFlush(SubredditPostJpa.testInstance(author = userJpa, subredditJpa = subredditJpa))

        val result = testee.findByName(expectedResult.name)

        assertThat(result).isEqualTo(expectedResult)
    }

}
