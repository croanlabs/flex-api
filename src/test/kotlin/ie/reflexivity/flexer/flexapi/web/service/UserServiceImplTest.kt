package ie.reflexivity.flexer.flexapi.web.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUser
import ie.reflexivity.flexer.flexapi.web.exceptions.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class UserServiceImplTest{

    private var userJpaRepository: UserJpaRepository = mock()

    @Test(expected = NotFoundException::class)
    fun `Given a user id that does not exist When fetching the user by Id Then NotFoundException should be raised`(){
        val testee = UserServiceImpl(userJpaRepository)
        val userId = "I_DONT_EXIST_ID"

        testee.fetchUser(userId)
    }

    @Test
    fun `Given a userId that exists When fetching a user by Id Then user should be loaded and returned`(){
        val userId = "anyUserId"
        val userJpa = UserJpa(id = 1,userId = userId,password = "anyPassword")
        val expectedResult = userJpa.toUser()
        whenever(userJpaRepository.findOneByUserId(userId)).thenReturn(userJpa)
        val testee = UserServiceImpl(userJpaRepository)

        val result = testee.fetchUser(userId)

        assertThat(result).isEqualTo(expectedResult)
    }

}
