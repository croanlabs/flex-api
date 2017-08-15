package ie.reflexivity.flexer.flexapi.resource

import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/greetings")
class GreetingsController {

    @GetMapping
    fun getGreeting()= ResponseEntity<String>("Hello There",OK)

}
