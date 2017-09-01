package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.model.Project


fun ProjectJpa.toProject() = Project(
        projectType = projectType,
        projectHomePage = projectHomePage
)
