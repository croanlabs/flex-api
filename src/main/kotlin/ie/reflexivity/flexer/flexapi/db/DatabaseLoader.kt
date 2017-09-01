package ie.reflexivity.flexer.flexapi.db

import ie.reflexivity.flexer.flexapi.SpringProfiles
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.model.ProjectType
import ie.reflexivity.flexer.flexapi.model.ProjectType.EOS
import ie.reflexivity.flexer.flexapi.model.ProjectType.ETHERUM
import ie.reflexivity.flexer.flexapi.model.ProjectType.TEZOS
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Profile(SpringProfiles.DEV_PROFILE)
class ApplicationEventListener(
        private val userJpaRepository: UserJpaRepository,
        private val projectJpaRepository: ProjectJpaRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun applicationStarted() {
        createUsers()
        createProjectStaticData();
    }

    @Transactional
    private fun createProjectStaticData() {
        val etherumProject = ProjectJpa(
                projectType = ETHERUM,
                projectHomePage = "https://ethereum.org/",
                githubUrl = "https://github.com/ethereum/",
                gitHubOrganisation = "ethereum"
        )
        createProjectIfDoesntExist(etherumProject)
        val tezusProject = ProjectJpa(
                projectType = TEZOS,
                projectHomePage = "https://www.tezos.com/",
                githubUrl = "https://github.com/tezos/tezos",
                gitHubOrganisation = "eosio"
        )
        createProjectIfDoesntExist(tezusProject)

        val eosProject = ProjectJpa(
                projectType = EOS,
                projectHomePage = "https://eos.io/",
                githubUrl = "http://github.com/eosio",
                gitHubOrganisation = "eosio"
        )
        createProjectIfDoesntExist(eosProject)
    }

    private fun createProjectIfDoesntExist(projectJpa: ProjectJpa) =
        projectJpaRepository.findOneByProjectType(projectJpa.projectType) ?: projectJpaRepository.save(projectJpa)


    @Transactional
    private fun createUsers() {
        userJpaRepository.deleteAll() // clean up from previous runs.
        val user1 = UserJpa(userId = "user1",password = "anyPassword")
        userJpaRepository.save(user1)
        val user2 = UserJpa(userId = "user2",password = "anyPassword")
        userJpaRepository.save(user2)
    }

}
