package ie.reflexivity.flexer.flexapi.client.github


fun okhttp3.Headers.toGitHubPage(): GitHubPage {
    var gitHubPage = GitHubPage()
    val linkHeader = this[GitHubConstants.HEADER_LINK]
    if (linkHeader != null) {
        val links = linkHeader.split(GitHubConstants.DELIM_LINKS.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (link in links) {
            val segments = link.split(GitHubConstants.DELIM_LINK_PARAM.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (segments.size < 2)
                continue

            var linkPart = segments[0].trim({ it <= ' ' })
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
            //$NON-NLS-1$ //$NON-NLS-2$
                continue
            linkPart = linkPart.substring(1, linkPart.length - 1)

            for (i in 1 until segments.size) {
                val rel = segments[i].trim({ it <= ' ' }).split("=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() //$NON-NLS-1$
                if (rel.size < 2 || !GitHubConstants.META_REL.equals(rel[0]))
                    continue

                var relValue = rel[1]
                if (relValue.startsWith("\"") && relValue.endsWith("\""))
                //$NON-NLS-1$ //$NON-NLS-2$
                    relValue = relValue.substring(1, relValue.length - 1)

                if (GitHubConstants.META_FIRST.equals(relValue))
                    gitHubPage = gitHubPage.copy(first = linkPart)
                else if (GitHubConstants.META_LAST.equals(relValue))
                    gitHubPage = gitHubPage.copy(last = linkPart)
                else if (GitHubConstants.META_NEXT.equals(relValue))
                    gitHubPage = gitHubPage.copy(next = linkPart)
                else if (GitHubConstants.META_PREV.equals(relValue))
                    gitHubPage = gitHubPage.copy(prev = linkPart)
            }
        }
    } else {
        gitHubPage = gitHubPage.copy(next = this[GitHubConstants.HEADER_NEXT])
        gitHubPage = gitHubPage.copy(last = this[GitHubConstants.HEADER_LAST])
    }
    return gitHubPage
}


fun okhttp3.Headers.rateLimit() =
        RateLimit(
                rateLimit = this[GitHubConstants.X_RATE_LIMIT_LIMIT]!!.toInt(),
                remainingLimit = this[GitHubConstants.X_RATE_LIMIT_REMAINING]!!.toInt(),
                resetEpochTime = this[GitHubConstants.X_RATE_LIMIT_RESET]!!.toLong()
        )
