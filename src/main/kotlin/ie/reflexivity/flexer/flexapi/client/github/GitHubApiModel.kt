package ie.reflexivity.flexer.flexapi.client.github

import ie.reflexivity.flexer.flexapi.db.domain.GitHubState
import ie.reflexivity.flexer.flexapi.extensions.toLocalDateTime
import java.time.LocalDateTime
import java.util.*


data class GitHubIssue(
        val title: String,
        val id: Int,
        val number: Int,
        val created_at: LocalDateTime?,
        val closed_at: LocalDateTime?,
        val state: GitHubState,
        val user: GitHubUser,
        val assignee: GitHubUser?,
        val assignees: List<GitHubUser>
)

data class GitHubUser(
        val login: String,
        val id: String,
        val email: String,
        val location: String,
        val company: String,
        val blog: String,
        val name: String,
        val avatarUrl: String,
        val followers: Int,
        val following: Int,
        val public_repos: Int,
        val public_gits: Int,
        val created_at: LocalDateTime
)

data class RateLimit(
        val rateLimit: Int,
        val remainingLimit: Int,
        val resetEpochTime: Long
) {
    fun resetDateTime() = Date(resetEpochTime * 1000).toLocalDateTime()

    fun isRemainLimitZero() = remainingLimit == 0

    fun isPastResetTime() = LocalDateTime.now().isAfter(resetDateTime())

    override fun toString(): String {
        return "RateLimit(rateLimit=$rateLimit, remainingLimit=$remainingLimit, resetEpochTime=${resetDateTime()}"
    }
}
