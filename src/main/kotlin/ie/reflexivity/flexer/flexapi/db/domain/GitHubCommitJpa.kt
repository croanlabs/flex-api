package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa.Companion.TABLE_NAME
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = TABLE_NAME)
data class GitHubCommitJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "author_id", referencedColumnName = "platformUserId"),
                JoinColumn(name = "author_platform", referencedColumnName = "platform"))
        val author: UserJpa, // person who done the work

        @ManyToOne
        @JoinColumns(JoinColumn(name = "committer_id", referencedColumnName = "platformUserId"),
                JoinColumn(name = "committer_platform", referencedColumnName = "platform"))
        val committer: UserJpa, // person who committed the changes

        @ManyToOne
        @JoinColumn(name = "repository_id")
        val repository: GitHubRepositoryJpa,

        @NotNull
        val shaId: String,

        val authorDate: LocalDateTime? = null,

        val commitDate: LocalDateTime? = null,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {
    companion object {
        internal const val TABLE_NAME = "github_commit"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

}
