package ie.reflexivity.flexer.flexapi.db.domain

import com.google.common.base.MoreObjects
import com.google.common.base.Objects
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa.Companion.TABLE_NAME
import ie.reflexivity.flexer.flexapi.model.ProjectType
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType.EAGER
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class ProjectJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = ALLOC_SIZE)
        @Column(name = ID_NAME, updatable = false, nullable = false)
        val id: Long = 0,

        @ManyToMany(cascade = arrayOf(ALL), fetch = EAGER)
        @JoinTable(name = "PROJECT_USER", joinColumns = arrayOf(JoinColumn(name = ID_NAME)),
                inverseJoinColumns = arrayOf(JoinColumn(name = "platformUserId", referencedColumnName = "platformUserId"),
                        JoinColumn(name = "platform", referencedColumnName = "platform")))
        val users: MutableSet<UserJpa>? = mutableSetOf(),

        @OneToMany(mappedBy = "project")
        val repositories: MutableSet<GitHubRepositoryJpa> = mutableSetOf(),

        @Enumerated(EnumType.STRING)
        @Column(unique = true, nullable = false)
        val projectType: ProjectType,

        val projectHomePage: String,

        val githubUrl: String,

        val gitHubOrganisation: String? = null,

        val gitHubRepository: String? = null,

        val gitHubLastScrapeRun: LocalDateTime? = null,

        @UpdateTimestamp
        val lastModified: LocalDateTime = LocalDateTime.now()

) {
    companion object {
        internal const val TABLE_NAME = "project"
        internal const val ID_NAME = TABLE_NAME + ID_SUFFIX
        private const val SEQUENCE_NAME = TABLE_NAME + SEQUENCE_SUFFIX
    }

    fun isGitOrganistation() = gitHubOrganisation != null

    fun isGitRepository() = gitHubRepository != null

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (other.javaClass != javaClass) return false

        other as ProjectJpa

        return Objects.equal(id, other.id)
                && Objects.equal(projectType, other.projectType)
                && Objects.equal(projectHomePage, other.projectHomePage)
                && Objects.equal(githubUrl, other.githubUrl)
                && Objects.equal(gitHubOrganisation, other.gitHubOrganisation)
                && Objects.equal(gitHubRepository, other.gitHubRepository)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, projectType, projectHomePage, githubUrl, gitHubOrganisation, gitHubRepository)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("projectType", projectType)
                .add("projectHomePage", projectHomePage)
                .add("githubUrl", githubUrl)
                .add("gitHubOrganisation", gitHubOrganisation)
                .add("gitHubRepository", gitHubRepository)
                .add("gitHubLastScrapeRun", gitHubLastScrapeRun)
                .add("lastModified", lastModified)
                .toString()
    }
}
