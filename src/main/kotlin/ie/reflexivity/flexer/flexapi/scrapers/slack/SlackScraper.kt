package ie.reflexivity.flexer.flexapi.scrapers.slack

import ie.reflexivity.flexer.flexapi.scrapers.slack.model.ChannelHistory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Stream

interface SlackScraper {
    fun scrape()
}

@Service
public class SlackScraperImpl():SlackScraper {


    override fun scrape() {

        val slackToken = "xoxp-22113863716-22113863956-248336256050-ec8a81680f971a98350555368ef14644"
        val slackHistoryFetcher = SlackHistoryFetcher.from(slackToken)
        val start = LocalDateTime.now()
        val end = LocalDateTime.now().minusDays(7)
        val (historyFrom, historyTo, messages) = slackHistoryFetcher.getChannelHistory(start, end)

        println(historyFrom.toString() + " / " + historyTo.toString())
        printMessages(messages
                .parallelStream())
    }


    fun printMessages(stream: Stream<ChannelHistory.Message>)  {
        stream.forEach(System.out::println)
    }

}