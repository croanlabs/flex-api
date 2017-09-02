package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.GitHubOrganisationJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface GitHubOrganisationJpaRepository : JpaRepository<GitHubOrganisationJpa, Long> {

    fun findByGitHubId(gitHubId: Int) : GitHubOrganisationJpa

}
