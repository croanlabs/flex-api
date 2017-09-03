package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa.Companion.TABLE_NAME
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE_NAME, indexes = arrayOf(
        Index(name = "IDX_${TABLE_NAME}_GITHUBID", columnList = "gitHubId")
))
data class GitHubRepositoryJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @JoinColumn(name = ProjectJpa.ID_NAME)
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        val projectJpa: ProjectJpa,

        @Column(unique = true)
        val gitHubId: Int,

        val name: String,

        val language: String? = null,

        val ownerName: String,

        val starGazersCount: Int = 0,

        val watchersCount: Int = 0,

        val forksCount: Int = 0,

        val openIssuesCount: Int = 0

) {
    companion object {
        internal const val TABLE_NAME = "git_hub_repository"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }
}
