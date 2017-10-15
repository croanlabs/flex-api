package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.client.github.GitHubIssue
import ie.reflexivity.flexer.flexapi.client.github.GitHubUser
import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubIssueJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitUser
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

fun GHUser.toUserJpa() = UserJpa(
        platform = GIT_HUB,
        platformId = id.toString(),
        platformUserId = login,
        email = email,
        location = location,
        company = company,
        blog = blog,
        name = name,
        avatarUrl = avatarUrl,
        created = createdAt?.toLocalDateTime(),
        gitHubFollowersCount = followersCount,
        gitHubFollowingCount = followingCount,
        gitHubPublicGistCount = publicGistCount,
        gitHubPublicRepoCount = publicRepoCount
)

fun GitHubUser.toUserJpa() = UserJpa(
        platform = GIT_HUB,
        platformId = id,
        platformUserId = login,
        email = email,
        location = location,
        company = company,
        blog = blog,
        name = name,
        avatarUrl = avatarUrl,
        created = created_at,
        gitHubFollowersCount = followers,
        gitHubFollowingCount = following,
        gitHubPublicGistCount = public_gits,
        gitHubPublicRepoCount = public_repos
)


fun GitUser.toUserJpa() = UserJpa(
        platform = GIT_HUB,
        platformId = email,
        platformUserId = email,
        email = email,
        name = name,
        created = date?.toLocalDateTime()
)


fun GHCommit.toCommitJpa(repositoryJpa: GitHubRepositoryJpa, author: UserJpa, committer: UserJpa) =
        GitHubCommitJpa(
                author = author,
                committer = committer,
                repository = repositoryJpa,
                commitDate = commitDate?.toLocalDateTime(),
                authorDate = authoredDate?.toLocalDateTime(),
                shaId = shA1,
                linesAdded = linesAdded,
                linesDeleted = linesDeleted,
                linesChanged = linesChanged

        )

fun GitHubIssue.toGitHubIssueJpa(gitHubRepositoryJpa: GitHubRepositoryJpa, createdBy: UserJpa, closedBy: UserJpa? = null) =
        GitHubIssueJpa(
                repository = gitHubRepositoryJpa,
                gitHubId = number,
                createdOn = created_at,
                closedOn = closed_at,
                createdBy = createdBy,
                state = state,
                title = title.shortenToDBMaxAllowedSize(),
                closedBy = closedBy
        )
