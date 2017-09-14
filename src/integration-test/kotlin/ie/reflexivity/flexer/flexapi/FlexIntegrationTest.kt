package ie.reflexivity.flexer.flexapi

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanDatabase
@ActiveProfiles(SpringProfiles.TEST_PROFILE)
annotation class FlexIntegrationTest
