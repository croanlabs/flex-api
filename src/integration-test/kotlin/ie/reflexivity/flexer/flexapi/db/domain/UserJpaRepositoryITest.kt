package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
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
class UserJpaRepositoryITest {

    @Inject private lateinit var userJpaRepository: UserJpaRepository
    @Inject private lateinit var projectJpaRepository: ProjectJpaRepository

    @Test
    fun `Given a project When adding a user Then expect the user to be associated with that project`() {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val userJpa = userJpaRepository.save(UserJpa.testInstance())

        val result = projectJpaRepository.save(projectJpa.copy(users = mutableSetOf(userJpa)))

        val reloadedProject = projectJpaRepository.findOne(result.id)
        assertThat(reloadedProject.users?.size).isEqualTo(1)
        assertThat(reloadedProject.users?.elementAt(0)).isEqualTo(userJpa)
    }

    @Test
    fun `Given a project which already has user When adding another user Then expect the user to be added to project`() {
        val projectJpa = createProjectWithUser()
        val anotherUser = UserJpa.testInstance().copy(platformUserId = "anotherUser", platform = REDDIT)
        val savedAnotherUser = userJpaRepository.save(anotherUser)

        projectJpa.users?.add(savedAnotherUser)
        projectJpaRepository.save(projectJpa)

        val reloadedProject = projectJpaRepository.findOne(projectJpa.id)
        assertThat(reloadedProject.users?.size).isEqualTo(2)
    }

    @Test
    fun `Given an existing user When searching by user Id and platform Then we expect the user to be returned`() {
        val savedUser = userJpaRepository.save(UserJpa.testInstance())

        val result = userJpaRepository.findByPlatformUserIdAndPlatform(savedUser.platformUserId, savedUser.platform)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(result)
    }

    private fun createProjectWithUser(): ProjectJpa {
        val projectJpa = projectJpaRepository.save(ProjectJpa.testInstance())
        val userJpa = userJpaRepository.save(UserJpa.testInstance())
        return projectJpaRepository.save(projectJpa.copy(users = mutableSetOf(userJpa)))
    }

}
