package ie.reflexivity.flexer.flexapi.config

import ie.reflexivity.flexer.flexapi.db.JPAConfig
import ie.reflexivity.flexer.flexapi.db.repository.UserRepository
import ie.reflexivity.flexer.flexapi.web.service.UserService
import ie.reflexivity.flexer.flexapi.web.service.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(JPAConfig::class)
class ServiceConfig{

    @Autowired
    private lateinit var userReposistory: UserRepository

    @Bean
    fun userService(): UserService = UserServiceImpl(userReposistory)
}
