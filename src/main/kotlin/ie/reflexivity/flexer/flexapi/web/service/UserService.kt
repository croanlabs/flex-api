package ie.reflexivity.flexer.flexapi.web.service

import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUser
import ie.reflexivity.flexer.flexapi.model.User
import org.springframework.stereotype.Service

interface UserService {
    fun fetchUsers(): List<User>
}

@Service
class UserServiceImpl(
        private val userJpaRepository: UserJpaRepository
) : UserService {

    override fun fetchUsers(): List<User> {
        val users = userJpaRepository.findAll()
        return users.map { e -> e.toUser() }
    }

}
