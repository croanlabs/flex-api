package ie.reflexivity.flexer.flexapi.web.resource

import ie.reflexivity.flexer.flexapi.web.MediaTypes
import ie.reflexivity.flexer.flexapi.web.service.ProjectService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/projects")
class ProjectController(
        private val projectService: ProjectService
){

    @GetMapping(produces = arrayOf(MediaTypes.APPLICATION_JSON))
    fun fetchProjects(pageable: Pageable)= projectService.fetchProjects(pageable)

}
