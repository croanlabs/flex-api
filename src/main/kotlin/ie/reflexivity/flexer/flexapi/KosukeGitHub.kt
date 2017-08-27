package ie.reflexivity.flexer.flexapi

import org.kohsuke.github.GHIssueState.ALL
import org.kohsuke.github.GitHub


private const val ORGANISATION = "ethereum"

//TODO how to handle paging
//TODO rate limit?
fun main(args: Array<String>) {
    val github = GitHub.connect()
    val organisation = github.getOrganization(ORGANISATION)
    val repositories = organisation.repositories.values
    repositories.forEach {
        System.out.println("Repo Name ${it.name}")
        System.out.println("Repo Name ${it.description}")
        System.out.println("Repo Name ${it.owner}")
        System.out.println("Repo Name ${it.openIssueCount}")
        val issues = it.listIssues(ALL)
        issues.toMutableList().forEach {
            System.out.println("Issues ${it.id} created on ${it.createdAt} with status of ${it.state}")
        }
    }

}
