package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa.Companion.TABLE_NAME
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table


@Entity
@Table(name = TABLE_NAME)
data class SubredditJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @JoinColumn(name = ProjectJpa.ID_NAME)
        @OneToOne(optional = true, fetch = FetchType.LAZY)
        val project: ProjectJpa? = null,

        @Column(unique = true)
        val redditId: String, // couldnt call subredditId due to table name id conflict

        val display_name: String,

        val active_user_count: Int,

        val accounts_active: Int,

        val subscribers: Int,

        val created: LocalDateTime,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {

    companion object {
        internal const val TABLE_NAME = "subreddit"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

}
