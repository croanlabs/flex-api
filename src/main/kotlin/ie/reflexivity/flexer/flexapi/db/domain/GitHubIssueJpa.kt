package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.GitHubIssueJpa.Companion.TABLE_NAME
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Transient
import javax.persistence.UniqueConstraint

@Entity
@Table(name = TABLE_NAME,
        uniqueConstraints = arrayOf(
                UniqueConstraint(columnNames = arrayOf("repository_id", "gitHubId"))
        )
)
data class GitHubIssueJpa(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "repository_id")
        val repository: GitHubRepositoryJpa,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "created_user_id", referencedColumnName = "platformUserId"),
                JoinColumn(name = "created_user_platform", referencedColumnName = "platform"))
        val createdBy: UserJpa,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "closedBy_user_id", referencedColumnName = "platformUserId"),
                JoinColumn(name = "closedBy_user_platform", referencedColumnName = "platform"))
        val closedBy: UserJpa?,

        val gitHubId: Int = 0,

        @Enumerated(EnumType.STRING)
        val state: GitHubState,

        val createdOn: LocalDateTime? = null,

        val closedOn: LocalDateTime? = null,

        val title: String? = null,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {
    companion object {
        internal const val TABLE_NAME = "git_hub_issue"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

    @Transient
    fun isOpen() = GitHubState.OPEN.equals(state)

}

enum class GitHubState {
    OPEN, CLOSED, ALL
}
