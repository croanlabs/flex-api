package ie.reflexivity.flexer.flexapi.client.github

/**
 * See for more constants.
 * https://github.com/eclipse/egit-github/blob/master/org.eclipse.egit.github.core/src/org/eclipse/egit/github/core/client/IGitHubConstants.java
 */
object GitHubConstants {
    val DELIM_LINKS = ","
    val DELIM_LINK_PARAM = ";"
    val META_REL = "rel"
    val META_LAST = "last"
    val META_NEXT = "next"
    val META_FIRST = "first"
    val META_PREV = "prev"
    val HEADER_LINK = "Link"
    val HEADER_NEXT = "X-Next"
    val HEADER_LAST = "X-Last"
    val PARAM_PAGE = "page"
    val X_RATE_LIMIT_LIMIT = "X-RateLimit-Limit"
    val X_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining"
    val X_RATE_LIMIT_RESET = "X-RateLimit-Reset"
}
