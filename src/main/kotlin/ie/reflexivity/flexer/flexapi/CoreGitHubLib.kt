package ie.reflexivity.flexer.flexapi

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.IssueService
import org.eclipse.egit.github.core.service.OrganizationService
import org.eclipse.egit.github.core.service.RepositoryService

private const val ORGANISATION = "ethereum"

/**
 * Lib API
 * https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core
 *
 * Throws Exception when rate limit exceeded
 *
 * org.eclipse.egit.github.core.client.NoSuchPageException: API rate limit exceeded for 62.46.33.200.
 * (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.) (403)
at org.eclipse.egit.github.core.client.PageIterator.next(PageIterator.java:175)
at org.eclipse.egit.github.core.client.PageIterator.next(PageIterator.java:42)
at ie.reflexivity.flexer.flexapi.CoreGitHubLibKt.main(CoreGitHubLib.kt:55)
Caused by: org.eclipse.egit.github.core.client.RequestException: API rate limit exceeded for 62.46.33.200. (But here's the good news:
Authenticated requests get a higher rate limit. Check out the documentation for more details.) (403)
... 2 more

 *
 */


fun main(args: Array<String>) {

    val gitHubClient = GitHubClient()
    val organisationService = OrganizationService(gitHubClient)
    val members = organisationService.getMembers(ORGANISATION)
    members.forEach {
        System.out.println("Members are ${it.login}")
    }
    System.out.println("Remaining requests ")

    val service = RepositoryService(gitHubClient)
    val repos = service.getOrgRepositories(ORGANISATION)

    System.out.println("Retrieved repos")
    val issueService = IssueService(gitHubClient)
    repos.forEach {
        System.out.println(getRequestLimitStats(gitHubClient)+ "RepoName  ${it.name} Description: ${it.description} Owner: ${it.owner} OpenIssues: ${it.openIssues}")

        val repoName = it.name
        val issues = issueService.pageIssues(it.owner.login,it.name)
        issues.forEach {
            System.out.println(getRequestLimitStats(gitHubClient) + "RepoName: $repoName NextPage=${issues.nextPage} LastPage=${issues.lastPage}")
            if (issues.hasNext()){
                it.forEach {
                    System.out.println("RepoName: $repoName Id: ${it.id}  state : ${it.state} ${it.createdAt} " +
                            "Closed: ${it.closedAt} LastUpdatedOn${it.updatedAt}")
                }
            }
        }
    }

}

fun getRequestLimitStats(gitHubClient: GitHubClient) = "${gitHubClient.remainingRequests} ${gitHubClient.requestLimit} "

