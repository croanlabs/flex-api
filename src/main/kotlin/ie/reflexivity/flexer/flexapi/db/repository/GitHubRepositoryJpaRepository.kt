package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface GitHubRepositoryJpaRepository : JpaRepository<GitHubRepositoryJpa, Long> {

    fun findByGitHubId(gitHubId: Int): GitHubRepositoryJpa?

}
