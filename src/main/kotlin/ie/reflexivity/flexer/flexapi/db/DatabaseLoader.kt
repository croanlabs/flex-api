package ie.reflexivity.flexer.flexapi.db

import ie.reflexivity.flexer.flexapi.SpringProfiles
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Profile(SpringProfiles.DEV_PROFILE)
class ApplicationEventListener(
        private val userRepository: UserRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun applicationStarted() {
        createUsers()
    }

    @Transactional
    private fun createUsers() {
        userRepository.deleteAll() // clean up from previous runs.
        val user1 = UserJpa(userId = "user1",password = "anyPassword")
        userRepository.save(user1)
        val user2 = UserJpa(userId = "user2",password = "anyPassword")
        userRepository.save(user2)
    }
}
