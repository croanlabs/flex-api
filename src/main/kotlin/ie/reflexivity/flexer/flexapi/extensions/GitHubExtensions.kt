package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser


fun GHOrganization.toGitHubOrganisationJpa(projectJpa: ProjectJpa) =
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

fun GHRepository.toGitHubRepositoryJpa(projectJpa: ProjectJpa) =
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

fun GHUser.toUserJpa(platform: Platform) = UserJpa(
        platform = platform,
        platformId = id.toString(),
        platformUserId = login,
        email = email,
        location = location,
        company = company,
        blog = blog,
        name = name,
        created = createdAt?.toLocalDateTime(),
        gitHubFollowersCount = followersCount,
        gitHubFollowingCount = followingCount,
        gitHubPublicGistCount = publicGistCount,
        gitHubPublicRepoCount = publicRepoCount
)
