package ie.reflexivity.flexer.flexapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class FlexApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(FlexApiApplication::class.java, *args)
}
