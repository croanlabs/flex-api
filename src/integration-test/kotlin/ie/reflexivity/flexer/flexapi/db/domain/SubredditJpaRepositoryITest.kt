package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.SubredditJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class SubredditJpaRepositoryITest {

    @Inject lateinit var subredditJpaRepository: SubredditJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a subbreddit When saving Then it should be retrievable from the database`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())

        val result = subredditJpaRepository.save(SubredditJpa.testInstance(projectJpa))

        assertThat(result).isEqualTo(subredditJpaRepository.findOne(result.id))
    }

    @Test
    fun `Given an existing subreddit When searching by reddit id Then it should be retrieved from the database`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val expectedResult = subredditJpaRepository.save(SubredditJpa.testInstance(projectJpa))

        val result = subredditJpaRepository.findByRedditId(expectedResult.redditId)

        assertThat(result).isEqualTo(expectedResult)
    }

}
