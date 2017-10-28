package ie.reflexivity.flexer.flexapi.scrapers.reddit

import ie.reflexivity.flexer.flexapi.client.reddit.api.SubredditUserApi
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.model.Platform.REDDIT
import ie.reflexivity.flexer.flexapi.web.exceptions.NotFoundException
import org.springframework.stereotype.Service

interface RedditUserService {
    fun createOrFetchUser(author: String): UserJpa
}

@Service
class RedditUserServiceImpl(
        private val userJpaRepository: UserJpaRepository,
        private val subredditUserApi: SubredditUserApi
) : RedditUserService {

    override fun createOrFetchUser(author: String): UserJpa {
        val userJpa = userJpaRepository.findByPlatformUserIdAndPlatform(author, REDDIT)
        if (userJpa != null) return userJpa
        val userResponse = subredditUserApi.fetchUser(author).execute()
        if (userResponse.isSuccessful == false)
            throw NotFoundException("Failed to find reddit user with id of $author")

        val newUserJpa = userResponse.body()!!.data.toUserJpa()
        return userJpaRepository.save(newUserJpa)
    }

}
