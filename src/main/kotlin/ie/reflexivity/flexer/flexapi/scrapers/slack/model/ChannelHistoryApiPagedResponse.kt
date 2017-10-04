package ie.reflexivity.flexer.flexapi.scrapers.slack.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class ChannelHistoryApiPagedResponse(@SerializedName("ok") val ok: Boolean,
                                          @SerializedName("messages") val apiMessages: List<ChannelHistoryApiMessage>,
                                          @SerializedName("has_more") val hasMore: Boolean,
                                          @SerializedName("unread_count_display") val unreadCountDisplay: BigInteger,
                                          @SerializedName("latest") val latest: String)

