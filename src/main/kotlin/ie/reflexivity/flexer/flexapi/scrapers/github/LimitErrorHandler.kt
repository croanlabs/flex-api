package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.logger
import org.kohsuke.github.AbuseLimitHandler
import org.kohsuke.github.RateLimitHandler
import java.io.IOException
import java.net.HttpURLConnection


class AbuseLimitErrorHandler : AbuseLimitHandler() {

    private val log by logger()

    override fun onError(e: IOException?, uc: HttpURLConnection?) {
        log.warn("Rate limit has been exceeded, will shutdown this cycle run")
        log.trace("Rate limit exception details ", e, uc)
        throw RateLimitException("Rate limit exceeded")
    }

}


class RateLimitErrorHandler : RateLimitHandler() {

    private val log by logger()

    override fun onError(e: IOException?, uc: HttpURLConnection?) {
        log.warn("Rate limit has been exceeded, will shutdown this cycle run")
        log.trace("Rate limit exception details ", e, uc)
        throw RateLimitException("Rate limit exceeded")
    }

}

