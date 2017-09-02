package ie.reflexivity.flexer.flexapi.db.domain

import ie.reflexivity.flexer.flexapi.db.repository.GitHubOrganisationJpaRepository
import ie.reflexivity.flexer.flexapi.test.infrastructure.testInstance
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import org.assertj.core.api.Assertions.*


@JpaRepositoryTest
@RunWith(SpringRunner::class)
class GitHubOrganisationJpaRepositoryITest {

    @Autowired private lateinit var  gitHubOrganisationJpaRepository : GitHubOrganisationJpaRepository

    @Test
    fun `Given a new github organisation When saved Then the organisation should be saved`(){
        val gitHubOrganisationJpa = GitHubOrganisationJpa.testInstance()

        val result = gitHubOrganisationJpaRepository.save(gitHubOrganisationJpa)

        assertThat(result).isNotNull()
        assertThat(result.id).isNotNull()
    }

    @Test
    fun `Given a saved organisation When searching by gitHubId that exists Then the github organisation should be returned`(){
        val gitHubId = 12
        val gitHubOrganisationJpa = GitHubOrganisationJpa.testInstance().copy(gitHubId = gitHubId)
        gitHubOrganisationJpaRepository.save(gitHubOrganisationJpa)

        val result = gitHubOrganisationJpaRepository.findByGitHubId(gitHubId)

        assertThat(result).isNotNull()
        assertThat(result.gitHubId).isEqualTo(gitHubId)
    }

}
