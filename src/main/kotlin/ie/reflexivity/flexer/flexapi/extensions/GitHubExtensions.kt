package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("ie.reflexivity.flexer.flexapi.extensions")
fun GitHub.printRateDetails() {
    val rateLimit = rateLimit
    log.info("RateLimit Limit=${rateLimit.limit} Remaining=${rateLimit.remaining} Reset=${rateLimit.reset}")
}

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
                project = projectJpa,
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

fun GHCommit.toCommitJpa(gitHubRepositoryJpa: GitHubRepositoryJpa) =
        GitHubCommitJpa(
                author = author.toUserJpa(GIT_HUB),
                committer = committer.toUserJpa(GIT_HUB),
                repository = gitHubRepositoryJpa,
                commitDate = commitDate?.toLocalDateTime(),
                authorDate = authoredDate?.toLocalDateTime(),
                shaId = shA1
        )
