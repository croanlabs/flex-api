package ie.reflexivity.flexer.flexapi.scrapers.slack.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.ArrayList


internal class ChannelHistoryApiFullResponse(private var lastResponseDateTime: LocalDateTime?) {
    private val responses = ArrayList<ChannelHistoryApiPagedResponse>()
    private var hasMore = false

    fun hasMoreResponses(): Boolean {
        return hasMore
    }

    fun addResponse(apiPagedChannelHistory: ChannelHistoryApiPagedResponse) {
        updateLastResponseDateTime(apiPagedChannelHistory)
        responses.add(apiPagedChannelHistory)
    }

    private fun updateLastResponseDateTime(apiPagedChannelHistory: ChannelHistoryApiPagedResponse) {
        val apiMessages = apiPagedChannelHistory.apiMessages
        val oldestApiMessage = apiMessages.get(apiMessages.size - 1)
        val lastResponseEpochTime = oldestApiMessage.timeStamp
        lastResponseDateTime = convertToLocalDateTime(lastResponseEpochTime)
        hasMore = apiPagedChannelHistory.hasMore
    }

    private fun convertToLocalDateTime(lastResponseEpochTime: String): LocalDateTime {
        val decimalSplit = lastResponseEpochTime.indexOf(".")
        val epochSecond = java.lang.Long.valueOf(lastResponseEpochTime.substring(0, decimalSplit))
        val nanoOfSecond = Integer.valueOf(lastResponseEpochTime.substring(decimalSplit + 1))
        val timezone = ZoneOffset.UTC
        return LocalDateTime.ofEpochSecond(epochSecond!!, nanoOfSecond!!, timezone)
    }

    val lastResponseEpochTime: String
        get() = lastResponseDateTime!!.toEpochSecond(ZoneOffset.UTC).toString()

    fun getResponses(): List<ChannelHistoryApiPagedResponse> {
        return responses
    }

    fun responsesAreAfter(dateTime: LocalDateTime): Boolean {
        val latestMessages = responses[responses.size - 1].apiMessages
        val latestMessage = latestMessages.get(latestMessages.size - 1)
        val latestMessageDateTime = convertToLocalDateTime(latestMessage.timeStamp)
        return latestMessageDateTime.isAfter(dateTime)
    }
}