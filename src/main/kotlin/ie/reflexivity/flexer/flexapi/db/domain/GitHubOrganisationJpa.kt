package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa.Companion.TABLE_NAME
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
data class GitHubOrganisationJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @JoinColumn(name = ProjectJpa.ID_NAME)
        @OneToOne(optional = false, fetch = FetchType.LAZY)
        val projectJpa: ProjectJpa,

        @Column(unique = true)
        val gitHubId: Int,

        val blog: String? = null,

        val email: String? = null,

        val company: String? = null,

        val location: String? = null,

        val noOfPublicRepos: Int? = 0,

        val htmlUrl: String? = null,

        val noOfPublicGists: Int? = 0,

        val noOfFollowers: Int? = 0,

        val following: Int? = 0,

        val createdAt: LocalDateTime? = null,

        val updatedAt: LocalDateTime? = null,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()


) {
    companion object {
        internal const val TABLE_NAME = "git_hub_organisation"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }
}
