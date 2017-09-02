package ie.reflexivity.flexer.flexapi.test.infrastructure

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.model.ProjectType.ETHERUM
import java.time.LocalDateTime


fun GitHubOrganisationJpa.Companion.testInstance(projectJpa: ProjectJpa = ProjectJpa.testInstance()) = GitHubOrganisationJpa(
        gitHubId = 12,
        blog = "anyBlog",
        email = "anyEmail",
        company = "anyCompany",
        location = "anyLocation",
        noOfPublicRepos = 10,
        noOfFollowers = 20,
        noOfPublicGists = 100,
        following = 100,
        htmlUrl = "http://any.com",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        projectJpa = projectJpa
)

fun ProjectJpa.Companion.testInstance() = ProjectJpa(
        projectType = ETHERUM,
        projectHomePage = "anyHomePage",
        githubUrl = "anyGitHubUrl",
        gitHubOrganisation = "anyGithubOrganisation"

)
