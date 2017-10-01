package ie.reflexivity.flexer.flexapi.client.github

import ie.reflexivity.flexer.flexapi.scrapers.github.RateLimitException
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

/**
 *  https://developer.github.com/v3/rate_limit/
 */
class RateLimitInterceptor : Interceptor {

    private var lastCallRateLimit: RateLimit? = null

    override fun intercept(chain: Chain): Response {
        if (lastCallRateLimit != null) {
            if (lastCallRateLimit!!.isRemainLimitZero() && lastCallRateLimit!!.isPastResetTime()) {
                throw RateLimitException("Git hub limit exceeded, call will not succeed. Last call details ${lastCallRateLimit}")
            }
        }
        val response = chain.proceed(chain.request())
        lastCallRateLimit = response.headers().rateLimit()
        if (response.code().equals(403) && lastCallRateLimit!!.isRemainLimitZero()) {
            throw RateLimitException("Git hub limit exceeded, call will not succeed. Last call details ${lastCallRateLimit}")
        }
        return response
    }
}
