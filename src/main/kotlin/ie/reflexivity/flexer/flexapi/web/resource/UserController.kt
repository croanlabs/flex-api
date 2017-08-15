package ie.reflexivity.flexer.flexapi.web.resource

import ie.reflexivity.flexer.flexapi.web.MediaTypes
import ie.reflexivity.flexer.flexapi.web.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
        private val userService: UserService
){

    @GetMapping(produces = arrayOf(MediaTypes.APPLICATION_JSON))
    fun fetchUsers()= userService.fetchUsers()

    @GetMapping(value = "/{id}", produces = arrayOf(MediaTypes.APPLICATION_JSON))
    fun fetchUser(@PathVariable("id") userId: String)= userService.fetchUser(userId)

}
