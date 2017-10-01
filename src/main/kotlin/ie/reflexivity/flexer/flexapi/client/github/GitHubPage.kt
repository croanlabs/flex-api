package ie.reflexivity.flexer.flexapi.client.github

import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.PARAM_PAGE
import org.springframework.web.util.UriComponentsBuilder

/**
 * https://developer.github.com/v3/guides/traversing-with-pagination/
 */
data class GitHubPage(
        val first: String? = null,
        val last: String? = null,
        val next: String? = null,
        val prev: String? = null
) {

    var nextPageNumber = parsePageNumber(next)

    fun hasNext() = next != null

    private fun parsePageNumber(uri: String?): Int {
        if (uri != null) {
            val param = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().get(PARAM_PAGE)!![0]
            if (param == null || param.length == 0)
                return -1
            try {
                return Integer.parseInt(param)
            } catch (nfe: NumberFormatException) {
            }
        }
        return -1
    }
}
