package ie.reflexivity.flexer.flexapi.web.service

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


interface ProjectService{
    fun fetchProjects(pageable: Pageable): Page<ProjectJpa>
}

@Service
class ProjectServiceImpl(
        private val projectJpaRepository: ProjectJpaRepository
): ProjectService{

    override fun fetchProjects(pageable: Pageable) = projectJpaRepository.findAll(pageable)
}
