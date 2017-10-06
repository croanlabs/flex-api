package ie.reflexivity.flexer.flexapi.client.reddit


data class SubRedditListing(
        val kind: String,
        val data: SubRedditData
)

data class SubRedditData(
        val children: List<SubRedditChild>,
        val after: String,
        val before: String
)

data class SubRedditChild(
        val kind: String,
        val data: SubRedditChildData
)

data class SubRedditChildData(
        val domain: String,
        val id: String,
        val title: String,
        val subreddit_id: String,
        val url: String,
        val author: String,
        val created: Long,
        val name: String,
        val view_count: Int,
        val num_crossposts: Int,
        val score: Int,
        val ups: Int,
        val num_comments: Int
)

data class RedditModerator(
        val author_flair_text: String,
        val name: String,
        val date : Long,
        val id : String
)
