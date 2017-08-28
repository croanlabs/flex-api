package ie.reflexivity.flexer.flexapi

import org.kohsuke.github.GHIssue
import org.kohsuke.github.GHIssueState.ALL
import org.kohsuke.github.GitHub
import org.kohsuke.github.PagedIterator

/**
 * http://github-api.kohsuke.org/
 *
 *
 */

// * To create a token
// https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/

private const val ORGANISATION = "ethereum"
private const val USER = "GitHubUserName"
private const val TOKEN = "addYouToken"

/*How to handle paging??
The page itertaor handles it for you, if you call next it will iterate under the hood through the pages.
See example below.
*/

//How does rate limit work ? See getRateLimit below, everytime you call ${github?.rateLimit} it triggers a call to backend, so you need to be prudent with the call here.
// When the rate is exceeded the library seems to "block", havent checked under the hood but no exceptionis thrown and application is still running.
fun main(args: Array<String>) {
    //val github = GitHub.connect(USER,TOKEN) // get you 5000 calls per hour.
    val github = GitHub.connectAnonymously() // gets you 60 calls per hours
    val organisation = github.getOrganization(ORGANISATION)
    organisation.listMembers().iterator().forEach {
        System.out.println("Member userName: ${it.login}")
    }
    val repositories = organisation.repositories.values
    repositories.forEach {
        System.out.println(getRateLimit(github) +" RepoName  ${it.name} Description: ${it.description} Owner: ${it.owner} OpenIssues: ${it.openIssueCount}")
        val issues = it.listIssues(ALL)
        val issuesIterator = issues.iterator()
        System.out.println("Number of issues ${issues.count()}")
        var issueCounter = 0
        while (issuesIterator.hasNext()) {
            issueCounter++
            val issue = issuesIterator.next()
            System.out.println("IssueCount $issueCounter Issues ${issue.id} created on ${issue.createdAt} with status of ${issue.state}")
        }
    }
}

/**
 * This does an active call to the API and doesnt return the rate limit from the last request.
 * So dont actively call it or performance will drop
 */
fun getRateLimit(github: GitHub?) = "${github?.rateLimit}"

