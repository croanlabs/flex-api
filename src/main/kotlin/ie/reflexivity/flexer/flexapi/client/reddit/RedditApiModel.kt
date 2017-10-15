package ie.reflexivity.flexer.flexapi.client.reddit


data class SubRedditListing<T>(
        val kind: String,
        val data: T
)

data class SubRedditData<T>(
        val children: List<SubredditListingChild<T>>,
        val after: String?,
        val before: String?
)

data class SubRedditList<T>(
        val children: List<T>
)


data class SubredditListingChild<T>(
        val kind: String,
        val data: T
)

data class SubredditPost(
        val domain: String,
        val id: String,
        val title: String,
        val subreddit_id: String,
        val subreddit: String,
        val url: String,
        val author: String,
        val created_utc: Long,
        val name: String, // unique identifer for the post. (Reddit name for it)
        val view_count: Int,
        val num_crossposts: Int,
        val score: Int,
        val ups: Int,
        val num_comments: Int
)

data class RedditModerator(
        val author_flair_text: String,
        val name: String,
        val date: Long,
        val id: String
)

data class SubredditAbout(
        val id: String,
        val display_name: String,
        val active_user_count: Int,
        val accounts_active: Int,
        val subscribers: Int,
        val created_utc: Long,
        val name: String // actually real unique id in reddit.
)

data class SubredditUser(
        val name: String,
        val id: String,
        val created_utc: Long,
        val is_mod: Boolean,
        val link_karma: Int,
        val comment_karma: Int,
        val has_verified_email: Boolean? = false,
        val verified: Boolean? = false
)

data class RedditRateLimit(
        val rateUsed: Int,
        val remainingLimit: Int,
        val resetSecs: Int
) {

    fun isRemainLimitZero() = remainingLimit == 0

    override fun toString(): String {
        return "RateLimit(rateLimit=$rateUsed, remainingLimit=$remainingLimit, resetSecs=${resetSecs}"
    }
}
