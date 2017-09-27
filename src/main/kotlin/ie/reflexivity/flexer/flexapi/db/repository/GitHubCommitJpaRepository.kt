package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface GitHubCommitJpaRepository : JpaRepository<GitHubCommitJpa, Long> {

    fun findFirstByRepositoryOrderByLastModifiedDesc(repository: GitHubRepositoryJpa): GitHubCommitJpa?

}
