package ie.reflexivity.flexer.flexapi.db.domain

import com.google.common.base.MoreObjects
import com.google.common.base.Objects
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa.Companion.TABLE_NAME
import ie.reflexivity.flexer.flexapi.model.Platform
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = TABLE_NAME)
@IdClass(UserId::class)
data class UserJpa(

        @Id
        val platformUserId: String,

        @Id
        val platform: Platform,

        @ManyToMany(mappedBy = "users")
        val projects: MutableSet<ProjectJpa>? = mutableSetOf(),

        val platformId: String? = null,

        val email: String? = null,

        val name: String? = null,

        val location: String? = null,

        val company: String? = null,

        val blog: String? = null,

        val created: LocalDateTime? = null,

        val gitHubFollowersCount: Int = 0,

        val gitHubFollowingCount: Int = 0,

        val gitHubPublicGistCount: Int = 0,

        val gitHubPublicRepoCount: Int = 0

) {
    companion object {
        internal const val TABLE_NAME = "user"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (other.javaClass != javaClass) return false

        other as UserJpa

        return Objects.equal(platform, other.platform)
                && Objects.equal(platformId, other.platformId)
                && Objects.equal(platformUserId, other.platformUserId)
                && Objects.equal(email, other.email)
                && Objects.equal(name, other.name)
                && Objects.equal(location, other.location)
                && Objects.equal(company, other.company)
                && Objects.equal(created, other.created)
                && Objects.equal(gitHubFollowersCount, other.gitHubFollowersCount)
                && Objects.equal(gitHubFollowingCount, other.gitHubFollowingCount)
                && Objects.equal(gitHubPublicGistCount, other.gitHubPublicGistCount)
                && Objects.equal(gitHubPublicRepoCount, other.gitHubPublicRepoCount)
    }


    override fun hashCode(): Int {
        return Objects.hashCode(platform, platformUserId, email, name, location, company, created, gitHubFollowersCount,
                gitHubFollowingCount, gitHubPublicGistCount, gitHubPublicRepoCount, platformId
        )
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("platformUserId", platformUserId)
                .add("platform", platform)
                .add("platformId", platformId)
                .add("email", email)
                .add("location", location)
                .add("company", company)
                .add("created", created)
                .add("gitHubFollowersCount", gitHubFollowersCount)
                .add("gitHubFollowingCount", gitHubFollowingCount)
                .add("gitHubPublicGistCount", gitHubPublicGistCount)
                .add("gitHubPublicRepoCount", gitHubPublicRepoCount)
                .add("name", name)

                .toString()
    }

}

data class UserId(
        @Column(length = 70, nullable = false)
        val platformUserId: String,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val platform: Platform

) : Serializable {

    val serialVersionUID: Long get() = 1L

    constructor() : this("", GIT_HUB)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (other.javaClass != javaClass) return false

        other as UserId

        return Objects.equal(platform, other.platform)
                && Objects.equal(platformUserId, other.platformUserId)
    }

    override fun hashCode() = Objects.hashCode(platform, platformUserId)

    override fun toString() = MoreObjects.toStringHelper(this)
            .add("platformUserId", platformUserId)
            .add("platform", platform)
            .toString()
}
