package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.UserRepository
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner

@JpaRepositoryTest
@RunWith(SpringRunner::class)
class UserRepositoryITest{

    @Autowired
    private lateinit var repository: UserRepository

    @Test
    fun `Given an existing users userId When fetching the user with the userId Then the expected user should be loaded`() {
        val userId = "anyUserId"
        saveUser(userId)

        val resultByUserId = repository.findOneByUserId(userId)

        assertThat(resultByUserId?.id).isNotNull()
        assertThat(resultByUserId?.userId).isEqualTo(userId)
    }

    private fun saveUser(userId: String) = repository.save(UserJpa(userId = userId, password = userId))

}
