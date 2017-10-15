package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.SubredditPostJpa.Companion.TABLE_NAME
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class SubredditPostJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @JoinColumn(name = SubredditJpa.ID_NAME)
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        val subreddit: SubredditJpa,

        @ManyToOne
        @JoinColumns(
                JoinColumn(name = "author_id", referencedColumnName = "platformUserId"),
                JoinColumn(name = "author_platform", referencedColumnName = "platform")
        )
        val author: UserJpa,

        val domain: String,

        val title: String,

        val url: String,

        val created: LocalDateTime,

        @Column(unique = true)
        val postId: String,

        val subredditName: String,

        val view_count: Int,

        val num_crossposts: Int,

        val score: Int,

        val ups: Int,

        val num_comments: Int,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {

    companion object {
        internal const val TABLE_NAME = "subreddit_post"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

}
