package ie.reflexivity.flexer.flexapi.web.service

import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUser
import ie.reflexivity.flexer.flexapi.model.User
import ie.reflexivity.flexer.flexapi.web.exceptions.NotFoundException
import org.springframework.stereotype.Service

interface UserService{
    fun fetchUsers(): List<User>
    fun fetchUser(userId: String): User
}

@Service
class UserServiceImpl(
        private val userJpaRepository: UserJpaRepository
):UserService{

    override fun fetchUser(userId: String):User{
        val userJpa = userJpaRepository.findOneByUserId(userId) ?: throw NotFoundException("Could not find user $userId")
        return userJpa.toUser()
    }

    override fun fetchUsers(): List<User>{
        val users = userJpaRepository.findAll()
        return users.map { e -> e.toUser() }
    }

}
