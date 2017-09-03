package ie.reflexivity.flexer.flexapi.db

import ie.reflexivity.flexer.flexapi.SpringProfiles
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.db.repository.UserJpaRepository
import ie.reflexivity.flexer.flexapi.model.ProjectType.ETHERUM
import ie.reflexivity.flexer.flexapi.model.ProjectType.GOLEM
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
                gitHubRepository = "tezos/tezos"
        )
        createProjectIfDoesntExist(tezusProject)

        val golemProject = ProjectJpa(
                projectType = GOLEM,
                projectHomePage = "https://golem.network/",
                githubUrl = "https://github.com/golemfactory/golem",
                gitHubRepository = "golemfactory/golem"
        )
        createProjectIfDoesntExist(golemProject)

/*
        val eosProject = ProjectJpa(
                projectType = EOS,
                projectHomePage = "https://eos.io/",
                githubUrl = "http://github.com/eosio",
                gitHubOrganisation = "eosio"
        )
        createProjectIfDoesntExist(eosProject)

        val aragonProject = ProjectJpa(
                projectType = ARAGON,
                projectHomePage = "https://aragon.one/",
                githubUrl = "https://github.com/aragon",
                gitHubOrganisation = "aragon"
        )
        createProjectIfDoesntExist(aragonProject)

        val ipfsProject = ProjectJpa(
                projectType = IPFS,
                projectHomePage = "https://github.com/ipfs",
                githubUrl = "https://github.com/ipfs",
                gitHubOrganisation = "ipfs"
        )
        createProjectIfDoesntExist(ipfsProject)

        val fileCoinProject = ProjectJpa(
                projectType = FILE_COIN,
                projectHomePage = "https://github.com/protocol",
                githubUrl = "https://github.com/protocol",
                gitHubOrganisation = "protocol"
        )
        createProjectIfDoesntExist(fileCoinProject)

        val humaniqProject = ProjectJpa(
                projectType = HUMANIG,
                projectHomePage = "https://humaniq.com/",
                githubUrl = "https://github.com/humaniq",
                gitHubOrganisation = "humaniq"
        )
        createProjectIfDoesntExist(humaniqProject)


        val steemProject = ProjectJpa(
                projectType = STEEM,
                projectHomePage = "https://steem.io/",
                githubUrl = "http://github.com/steemit",
                gitHubOrganisation = "steemit"
        )
        createProjectIfDoesntExist(steemProject)

        val nemProject = ProjectJpa(
                projectType = NEM,
                projectHomePage = "https://www.nem.io/",
                githubUrl = "https://github.com/NemProject",
                gitHubOrganisation = "NemProject"
        )
        createProjectIfDoesntExist(nemProject)

        val iotaProject = ProjectJpa(
                projectType = IOTA,
                projectHomePage = "https://iota.org/",
                githubUrl = "https://github.com/iotaledger",
                gitHubOrganisation = "iotaledger"
        )
        createProjectIfDoesntExist(iotaProject)
*/
    }

    private fun createProjectIfDoesntExist(projectJpa: ProjectJpa) =
            projectJpaRepository.findOneByProjectType(projectJpa.projectType) ?: projectJpaRepository.save(projectJpa)


    @Transactional
    private fun createUsers() {
        userJpaRepository.deleteAll() // clean up from previous runs.
        val user1 = UserJpa(userId = "user1", password = "anyPassword")
        userJpaRepository.save(user1)
        val user2 = UserJpa(userId = "user2", password = "anyPassword")
        userJpaRepository.save(user2)
    }

}
