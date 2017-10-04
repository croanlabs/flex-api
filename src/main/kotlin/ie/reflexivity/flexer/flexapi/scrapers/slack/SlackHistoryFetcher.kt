package ie.reflexivity.flexer.flexapi.scrapers.slack

import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiPagedResponse
import java.io.IOException
import java.time.LocalDateTime
import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistory
import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiFullResponse
import ie.reflexivity.flexer.flexapi.scrapers.slack.utils.ChannelHistoryConverter
import retrofit2.Response


class SlackHistoryFetcher {

    private val NUMBER_OF_RESULTS = 200

    private lateinit var token: String
    private lateinit var channel: String
    private lateinit var channelHistoryConverter: ChannelHistoryConverter


    val slackScraperApi: SlackApi = SlackApi()

    companion object {
        fun from(slackToken: String): SlackHistoryFetcher {
            val CHANNEL_GENERAL = "C0N3HM6MV"
            val channelHistoryConverter = ChannelHistoryConverter()
            return SlackHistoryFetcher(slackToken, CHANNEL_GENERAL, channelHistoryConverter)
        }

    }

    constructor(slackToken: String, channel: String, channelHistoryConverter:ChannelHistoryConverter) {
        this.token = slackToken
        this.channel = channel
        this.channelHistoryConverter = channelHistoryConverter
    }


    fun getChannelHistory(start: LocalDateTime, end: LocalDateTime): ChannelHistory {
        return channelHistoryConverter.convert(getApiChannelHistory(start, end))
    }

    private fun getApiChannelHistory(start: LocalDateTime, end: LocalDateTime): List<ChannelHistoryApiPagedResponse> {
        val apiPagedResponse = ChannelHistoryApiFullResponse(start)
        do {
            appendChannelHistory(apiPagedResponse)
        } while (apiPagedResponse.responsesAreAfter(end) && apiPagedResponse.hasMoreResponses())
        return apiPagedResponse.getResponses()
    }

    private fun appendChannelHistory(apiPagedResponse: ChannelHistoryApiFullResponse) {
        val response = fetchApiChannelHistoryResponse(apiPagedResponse.lastResponseEpochTime)
        if (response.isSuccessful) {
            val apiPagedChannelHistory = response.body()
            apiPagedChannelHistory?.let {
                apiPagedResponse.addResponse(it)
            }

        } else {
            throw IllegalStateException("No internet or server down or something."
                    + " code: " + response.code()
                    + " error: " + response.body())
        }
    }

    private fun fetchApiChannelHistoryResponse(lastResponseEpochTime: String): Response<ChannelHistoryApiPagedResponse> {

        val channelHistory = slackScraperApi.getChannelsHistory(token = token,
                channel = channel,
                count = NUMBER_OF_RESULTS,
                latest = lastResponseEpochTime)

        try {
            return channelHistory.execute()
        } catch (e: IOException) {
            throw IllegalStateException("FooBar ", e)
        }

    }

}