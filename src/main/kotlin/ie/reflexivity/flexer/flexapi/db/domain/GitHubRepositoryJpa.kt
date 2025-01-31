package ie.reflexivity.flexer.flexapi.db.domain

import com.google.common.base.Objects
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa.Companion.TABLE_NAME
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.time.LocalDateTime
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.FetchType.EAGER
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE_NAME, indexes = arrayOf(
        Index(name = "IDX_${TABLE_NAME}_GITHUBID", columnList = "gitHubId")
))
@Audited
data class GitHubRepositoryJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @JoinColumn(name = ProjectJpa.ID_NAME)
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @NotAudited
        val project: ProjectJpa,

        @ManyToMany(cascade = arrayOf(ALL), fetch = EAGER)
        @JoinTable(name = "REPOSITORY_USER", joinColumns = arrayOf(JoinColumn(name = ProjectJpa.ID_NAME)),
                inverseJoinColumns = arrayOf(JoinColumn(name = "platformUserId", referencedColumnName = "platformUserId"),
                        JoinColumn(name = "platform", referencedColumnName = "platform")))
        @NotAudited
        val collaborators: MutableSet<UserJpa> = mutableSetOf(),

        @OneToMany(mappedBy = "repository")
        @NotAudited
        val commits: MutableSet<GitHubCommitJpa> = mutableSetOf(),

        @OneToMany(mappedBy = "repository")
        @NotAudited
        val issues: MutableSet<GitHubIssueJpa> = mutableSetOf(),

        @Column(unique = true)
        val gitHubId: Int,

        val name: String,

        val language: String? = null,

        val ownerName: String,

        val starGazersCount: Int = 0,

        val watchersCount: Int = 0,

        val forksCount: Int = 0,

        val openIssuesCount: Int = 0,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {
    companion object {
        internal const val TABLE_NAME = "git_hub_repository"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

    override fun toString(): String {
        return "GitHubRepositoryJpa(id=$id, project=$project," +
                "gitHubId=$gitHubId, name='$name', " +
                "language=$language, ownerName='$ownerName', starGazersCount=$starGazersCount, " +
                "watchersCount=$watchersCount, forksCount=$forksCount, " +
                "openIssuesCount=$openIssuesCount, lastModified=$lastModified)"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (other.javaClass != javaClass) return false

        other as GitHubRepositoryJpa

        return Objects.equal(id, other.id)
                && Objects.equal(gitHubId, other.gitHubId)
                && Objects.equal(name, other.name)
                && Objects.equal(language, other.language)
                && Objects.equal(ownerName, other.ownerName)
                && Objects.equal(starGazersCount, other.starGazersCount)
                && Objects.equal(watchersCount, other.watchersCount)
                && Objects.equal(forksCount, other.forksCount)
                && Objects.equal(openIssuesCount, other.openIssuesCount)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, gitHubId, name, language,
                ownerName, starGazersCount, watchersCount, forksCount, openIssuesCount)
    }
}
