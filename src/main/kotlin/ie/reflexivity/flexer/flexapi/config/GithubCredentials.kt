package ie.reflexivity.flexer.flexapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubCredentials{

    @Value("\${github.username}")
    var username: String? = null

    @Value("\${github.token}")
    var token: String? = null


}
