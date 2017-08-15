package ie.reflexivity.flexer.flexapi.web.resource

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerITest{

    /** As we are using dynamic ports, we need to inject it here for later use when building the access token URI. */
    @LocalServerPort @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private lateinit var port: Integer

    private val baseUrl: String by lazy { "http://localhost:$port" }

    private val usersPath = "/api/users"

    private val rest = RestTemplate()

    @Test
    fun `fetching Users Should Return HTTP Code 200`() {
        val request = RequestEntity<Any>(GET, uri(usersPath))

        val response = rest.exchange(request, Any::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    protected fun url(path: String) = baseUrl + path

    protected fun uri(path: String) = URI.create(url(path))!!

}

