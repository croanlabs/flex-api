package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubIssueJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubRepositoryJpa
import ie.reflexivity.flexer.flexapi.db.domain.GitHubState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GitHubIssueJpaRepository : JpaRepository<GitHubIssueJpa, Long> {

    @Query("SELECT i FROM GitHubIssueJpa i WHERE i.gitHubId=:gitHubId AND i.repository.id=:repo_id")
    fun fetchIssue(@Param("gitHubId") gitHubId: Int, @Param("repo_id") repositoryId: Long): GitHubIssueJpa?

    fun findFirstByRepositoryAndStateOrderByCreatedOn(repository: GitHubRepositoryJpa, state: GitHubState): GitHubIssueJpa?

    fun findFirstByRepositoryOrderByCreatedOnDesc(repository: GitHubRepositoryJpa): GitHubIssueJpa?
}
