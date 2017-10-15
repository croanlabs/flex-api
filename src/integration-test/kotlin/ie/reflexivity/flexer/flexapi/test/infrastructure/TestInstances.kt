package ie.reflexivity.flexer.flexapi.test.infrastructure

import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubIssueJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState.OPEN
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.domain.SubredditPostJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import ie.reflexivity.flexer.flexapi.model.ProjectType.ETHERUM
import java.time.LocalDateTime


fun GitHubOrganisationJpa.Companion.testInstance(projectJpa: ProjectJpa = ProjectJpa.testInstance()) =
        GitHubOrganisationJpa(
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

fun ProjectJpa.Companion.testInstance(users: MutableSet<UserJpa> = mutableSetOf()) =
        ProjectJpa(
                projectType = ETHERUM,
                projectHomePage = "anyHomePage",
                githubUrl = "anyGitHubUrl",
                gitHubOrganisation = "anyGithubOrganisation",
                users = users
        )

fun UserJpa.Companion.testInstance(platform: Platform = GIT_HUB) =
        UserJpa(
                platformId = "1234",
                platformUserId = "anyPlatformId",
                platform = platform,
                email = "anyEmail",
                name = "anyName",
                location = "anyLocation",
                company = "anyCompany",
                created = LocalDateTime.now()
        )

fun GitHubRepositoryJpa.Companion.testIntance(projectJpa: ProjectJpa = ProjectJpa.testInstance()) =
        GitHubRepositoryJpa(
                project = projectJpa,
                gitHubId = 10,
                language = "anyLanguage",
                name = "anyName",
                ownerName = "anyOwnerLogin",
                starGazersCount = 10,
                watchersCount = 10,
                forksCount = 10,
                openIssuesCount = 100
        )

fun GitHubCommitJpa.Companion.testInstance(
        repository: GitHubRepositoryJpa,
        authorAndCommitter: UserJpa = UserJpa.testInstance()
) = GitHubCommitJpa(
        author = authorAndCommitter,
        committer = authorAndCommitter,
        repository = repository,
        shaId = "anyShaId",
        authorDate = LocalDateTime.now(),
        commitDate = LocalDateTime.now()
)

fun GitHubIssueJpa.Companion.testInstance(gitHubRepository: GitHubRepositoryJpa,
                                          creator: UserJpa,
                                          state: GitHubState = OPEN,
                                          createdOn: LocalDateTime = LocalDateTime.now().minusDays(2),
                                          gitHubId: Int = 100,
                                          closedOn: LocalDateTime = LocalDateTime.now()
) = GitHubIssueJpa(
        repository = gitHubRepository,
        createdBy = creator,
        gitHubId = gitHubId,
        state = state,
        createdOn = createdOn,
        closedOn = closedOn,
        closedBy = creator
)

fun SubredditJpa.Companion.testInstance(project: ProjectJpa) =
        SubredditJpa(
                project = project,
                redditId = "anyRedditId",
                display_name = "anyDisplayName",
                active_user_count = 100,
                accounts_active = 100,
                subscribers = 100,
                created = LocalDateTime.now()
        )

fun SubredditPostJpa.Companion.testInstance(subredditJpa: SubredditJpa, author: UserJpa) =
        SubredditPostJpa(
                subreddit = subredditJpa,
                author = author,
                domain = "anyDomain.com",
                title = "anyTitle",
                url = "anyUrl",
                created = LocalDateTime.now(),
                subredditName = "anySubredditName",
                postId = "anyPostId",
                view_count = 100,
                num_crossposts = 100,
                score = 100,
                ups = 100,
                num_comments = 100
        )
