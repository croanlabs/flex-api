package ie.reflexivity.flexer.flexapi.scrapers.slack.utils

import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistory
import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiMessage
import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiPagedResponse
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.ArrayList



class ChannelHistoryConverter {

    fun convert(apiPagedChannelHistory: List<ChannelHistoryApiPagedResponse>): ChannelHistory {
        val messages = ArrayList<ChannelHistory.Message>()
        for (pagedChannelHistory in apiPagedChannelHistory) {
            for (apiMessage in pagedChannelHistory.apiMessages) {
                messages.add(convert(apiMessage))
            }
        }
        val historyFrom = getOldestMessageLocalDateTime(apiPagedChannelHistory[apiPagedChannelHistory.size - 1])
        val historyTo = getLatestMessageLocalDateTime(apiPagedChannelHistory[0])
        return ChannelHistory(historyFrom, historyTo, messages)
    }

    private fun convert(apiMessage: ChannelHistoryApiMessage): ChannelHistory.Message {
        val text = apiMessage.text // Bots send apiMessages with attachments but no text
        return ChannelHistory.Message(text ?: "")
    }

    private fun getOldestMessageLocalDateTime(apiPagedChannelHistory: ChannelHistoryApiPagedResponse): LocalDateTime {
        val apiMessages = apiPagedChannelHistory.apiMessages
        val lastMessage = apiMessages.get(apiMessages.size - 1)
        return convertToLocalDateTime(lastMessage.timeStamp)
    }

    private fun getLatestMessageLocalDateTime(apiPagedChannelHistory: ChannelHistoryApiPagedResponse): LocalDateTime {
        val apiMessages = apiPagedChannelHistory.apiMessages
        val firstMessage = apiMessages.get(0)
        return convertToLocalDateTime(firstMessage.timeStamp)
    }

    private fun convertToLocalDateTime(lastResponseEpochTime: String): LocalDateTime {
        val decimalSplit = lastResponseEpochTime.indexOf(".")
        val epochSecond = java.lang.Long.valueOf(lastResponseEpochTime.substring(0, decimalSplit))
        val nanoOfSecond = Integer.valueOf(lastResponseEpochTime.substring(decimalSplit + 1))
        val timezone = ZoneOffset.UTC
        return LocalDateTime.ofEpochSecond(epochSecond!!, nanoOfSecond!!, timezone)
    }
}