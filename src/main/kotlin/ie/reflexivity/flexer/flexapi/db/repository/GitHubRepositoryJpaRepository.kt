package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository


@Repository
interface GitHubRepositoryJpaRepository :
        RevisionRepository<GitHubRepositoryJpa, Long, Int>,
        JpaRepository<GitHubRepositoryJpa, Long> {

    fun findByGitHubId(gitHubId: Int): GitHubRepositoryJpa?

}
