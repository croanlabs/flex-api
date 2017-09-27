package ie.reflexivity.flexer.flexapi.web.resource

import ie.reflexivity.flexer.flexapi.scrapers.github.GitHubScraper
import ie.reflexivity.flexer.flexapi.web.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scraper")
class ScraperController(
        val gitHubScraper: GitHubScraper
) {

    @GetMapping(produces = arrayOf(MediaTypes.APPLICATION_JSON))
    fun triggerGithubScrape() = gitHubScraper.scrape()

}
