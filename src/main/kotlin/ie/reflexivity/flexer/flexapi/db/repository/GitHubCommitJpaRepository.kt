package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubCommitJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface GitHubCommitJpaRepository : JpaRepository<GitHubCommitJpa, Long> {

}
