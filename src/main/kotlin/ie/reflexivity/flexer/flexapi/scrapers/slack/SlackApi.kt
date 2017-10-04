package ie.reflexivity.flexer.flexapi.scrapers.slack


import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistoryApiPagedResponse
import okhttp3.ResponseBody
import java.util.*
import io.reactivex.Observable
import retrofit2.Call
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

public class SlackApi {

    val slackService: SlackService = SlackServiceManager.getInstance()


    fun getChannelsHistory(token:String,
                           channel:String,
                           count:Int=100,
                           inclusive:Boolean = false,
                           latest:String = getCurrentEpochTime(),
                           oldest:String = "0",
                           unreads:Boolean = false ): Call<ChannelHistoryApiPagedResponse> {

        return slackService.channelsHistory(
                token = token,
                channel = channel,
                count = count,
                inclusive = inclusive,
                latest = latest,
                oldest = oldest,
                unreads = unreads)
    }


    fun getCurrentEpochTime():String {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
    }

}