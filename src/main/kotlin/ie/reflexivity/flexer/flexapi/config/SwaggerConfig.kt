package ie.reflexivity.flexer.flexapi.config

import ie.reflexivity.flexer.flexapi.SpringProfiles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@Profile(SpringProfiles.NOT_TEST_PROFILE)
class SwaggerConfiguration {

    @Bean
    fun swaggerDocket() =
            Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(ApiInfoBuilder()
                            .title("Flex API")
                            .description("API Documentation For Flex")
                            .build())
                    .groupName("FlexAPI")
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("ie.reflexivity.flexer"))
                    .paths(PathSelectors.any())
                    .build()

}
