package ie.reflexivity.flexer.flexapi.web.service

import ie.reflexivity.flexer.flexapi.db.repository.UserRepository
import ie.reflexivity.flexer.flexapi.extensions.toUser
import ie.reflexivity.flexer.flexapi.model.User
import ie.reflexivity.flexer.flexapi.web.exceptions.NotFoundException

interface UserService{
    fun fetchUsers(): List<User>
    fun fetchUser(userId: String): User
}

class UserServiceImpl(
        private val userRepository: UserRepository
):UserService{

    override fun fetchUser(userId: String):User{
        val userJpa = userRepository.findOneByUserId(userId) ?: throw NotFoundException("Could not find user $userId")
        return userJpa.toUser()
    }

    override fun fetchUsers() :List<User>{
        val users = userRepository.findAll()
        return users.map { e -> e.toUser() }
    }

}
