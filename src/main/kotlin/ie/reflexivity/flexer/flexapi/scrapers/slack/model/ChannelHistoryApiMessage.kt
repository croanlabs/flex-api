package ie.reflexivity.flexer.flexapi.scrapers.slack.model

import com.google.gson.annotations.SerializedName

data class ChannelHistoryApiMessage(@SerializedName("text") val text: String,
                          @SerializedName("type") val type: String,
                          @SerializedName("user") val user: String,
                          @SerializedName("ts") val timeStamp: String)