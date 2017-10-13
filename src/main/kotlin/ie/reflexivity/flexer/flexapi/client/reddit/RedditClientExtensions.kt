package ie.reflexivity.flexer.flexapi.client.reddit

import okhttp3.Headers
import okhttp3.Response


fun Response.rateLimit() = headers().rateLimit()

fun Headers.rateLimit() =
        RedditRateLimit(
                rateUsed = this[RedditConstants.X_RATE_LIMIT_USED]!!.toInt(),
                remainingLimit = this[RedditConstants.X_RATE_LIMIT_REMAINING]!!.toInt(),
                resetSecs = this[RedditConstants.X_RATE_LIMIT_RESET]!!.toInt()
        )
