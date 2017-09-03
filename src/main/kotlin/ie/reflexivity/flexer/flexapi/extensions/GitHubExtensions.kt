package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository


fun GHOrganization.toGitHubOrganisationJpa(projectJpa : ProjectJpa) =
        GitHubOrganisationJpa(
                gitHubId = id,
                email = email,
                blog = blog,
                company = company,
                location = location,
                noOfPublicRepos = publicRepoCount,
                htmlUrl = htmlUrl?.toString(),
                noOfPublicGists = publicGistCount,
                noOfFollowers = followersCount,
                following = followingCount,
                createdAt = createdAt?.toLocalDateTime(),
                updatedAt = updatedAt?.toLocalDateTime(),
                projectJpa = projectJpa
        )

fun GHRepository.toGitHubRepositoryJpa(projectJpa : ProjectJpa) =
        GitHubRepositoryJpa(
                projectJpa = projectJpa,
                name = name,
                ownerName = ownerName,
                gitHubId = id,
                language = language,
                starGazersCount = stargazersCount,
                watchersCount = watchers,
                forksCount = forks,
                openIssuesCount = openIssueCount
        )
