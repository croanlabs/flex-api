package ie.reflexivity.flexer.flexapi.scrapers.slack.model

import java.time.LocalDateTime

data class ChannelHistory(val historyFrom: LocalDateTime,
                     val historyTo: LocalDateTime,
                     val messages: List<Message>) {

    data class Message(val text:String)
}
