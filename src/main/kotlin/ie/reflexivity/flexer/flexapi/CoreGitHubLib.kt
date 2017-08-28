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
Caused by: org.eclipse.egit.github.core.client.RequestException: API rate limit exceeded for 62.46.33.200. (But here's the good news:
Authenticated requests get a higher rate limit. Check out the documentation for more details.) (403)
... 2 more
 */

//How does paging work? -> See issues below.
//Rate limit -> See getRequestLimitStats, different to Kosuke library where it updates the limit from the last request returned headers.

private const val TOKEN = "ADD_YOUR_TOKEN"

fun main(args: Array<String>) {
    //val gitHubClient = GitHubClient() // Without a token you get 60 calls an hour
    val gitHubClient = GitHubClient().setOAuth2Token(TOKEN) // with token you get 5,000 per hour
    val organisationService = OrganizationService(gitHubClient)
    val members = organisationService.getMembers(ORGANISATION)
    members.forEach {
        System.out.println("Members are ${it.login}")
    }

    val service = RepositoryService(gitHubClient)
    val repos = service.getOrgRepositories(ORGANISATION)
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

