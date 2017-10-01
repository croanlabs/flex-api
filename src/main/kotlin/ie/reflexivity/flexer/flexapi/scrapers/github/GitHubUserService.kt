package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.client.github.GitHubUser
import ie.reflexivity.flexer.flexapi.client.github.api.GitHubUserApiService
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.springframework.stereotype.Service


interface GitHubUserService {
    fun fetchOrCreate(user: GitHubUser): UserJpa
}

@Service
class GitHubUserServiceImpl(
        private val userJpaRepository: UserJpaRepository,
        private val gitHubUserApiService: GitHubUserApiService
) : GitHubUserService {

    override fun fetchOrCreate(user: GitHubUser): UserJpa {
        val userJpa = userJpaRepository.findByPlatformUserIdAndPlatform(user.login, GIT_HUB)
        if (userJpa == null) {
            val call = gitHubUserApiService.fetchUser(user.login)
            val gitHubUser = call.execute().body()
            return userJpaRepository.save(gitHubUser!!.toUserJpa())
        }
        return userJpa
    }

}
