package ie.reflexivity.flexer.flexapi.scrapers.reddit

import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials
import net.dean.jraw.paginators.SubredditPaginator
import java.util.*


class RedditScraper


fun main(args: Array<String>) {

    val myUserAgent = UserAgent.of("desktop", "net.dean.awesomescript", "v0.1", "thatJavaNerd")
    val redditClient = RedditClient(myUserAgent)
    val credentials = Credentials.userless("_3QgWk2uSnqcmw", "jrIEieleMKPsvSnGULQXsOuEVE4", UUID.randomUUID())
    val authData = redditClient.oAuthHelper.easyAuth(credentials)
    redditClient.authenticate(authData);
    println(redditClient.currentRatelimit)


    val subbreddit = redditClient.getSubreddit("melonproject")
    val submissions = SubredditPaginator(redditClient, "melonproject").next()

    submissions.forEach {
        println("${it.id} ${it.author} ${it.title}")
    }

}


