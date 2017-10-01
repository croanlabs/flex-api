package ie.reflexivity.flexer.flexapi.client.github

import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.DELIM_LINKS
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.DELIM_LINK_PARAM
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.HEADER_LAST
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.HEADER_LINK
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.HEADER_NEXT
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.META_FIRST
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.META_LAST
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.META_NEXT
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.META_PREV
import ie.reflexivity.flexer.flexapi.client.github.GitHubConstants.META_REL

/**
 * Logic taken from
 * https://github.com/eclipse/egit-github/blob/master/org.eclipse.egit.github.core/src/org/eclipse/egit/github/core/client/PageLinks.java
 */
class GitHubPageLinks {

    fun parseGitHubLinks(headers: okhttp3.Headers): GitHubPage {
        var gitHubPage = GitHubPage()
        val linkHeader = headers[HEADER_LINK]
        if (linkHeader != null) {
            val links = linkHeader.split(DELIM_LINKS.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (link in links) {
                val segments = link.split(DELIM_LINK_PARAM.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                if (segments.size < 2)
                    continue

                var linkPart = segments[0].trim({ it <= ' ' })
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
                //$NON-NLS-1$ //$NON-NLS-2$
                    continue
                linkPart = linkPart.substring(1, linkPart.length - 1)

                for (i in 1 until segments.size) {
                    val rel = segments[i].trim({ it <= ' ' }).split("=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() //$NON-NLS-1$
                    if (rel.size < 2 || !META_REL.equals(rel[0]))
                        continue

                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\""))
                    //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length - 1)

                    if (META_FIRST.equals(relValue))
                        gitHubPage = gitHubPage.copy(first = linkPart)
                    else if (META_LAST.equals(relValue))
                        gitHubPage = gitHubPage.copy(last = linkPart)
                    else if (META_NEXT.equals(relValue))
                        gitHubPage = gitHubPage.copy(next = linkPart)
                    else if (META_PREV.equals(relValue))
                        gitHubPage = gitHubPage.copy(prev = linkPart)
                }
            }
        } else {
            gitHubPage = gitHubPage.copy(next = headers[HEADER_NEXT])
            gitHubPage = gitHubPage.copy(last = headers[HEADER_LAST])
        }
        return gitHubPage
    }

}
