package ie.reflexivity.flexer.flexapi.scrapers.github

import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.repository.ProjectJpaRepository
import ie.reflexivity.flexer.flexapi.extensions.toUserJpa
import ie.reflexivity.flexer.flexapi.logger
import ie.reflexivity.flexer.flexapi.model.Platform.GIT_HUB
import org.kohsuke.github.GHUser
import org.kohsuke.github.PagedIterable
import org.springframework.stereotype.Service

interface GitHubMembersScraper {
    fun scrape(members: PagedIterable<GHUser>, detachedProjectJpa: ProjectJpa)
}

@Service
class GitHubMembersScraperImpl(
        private val projectJpaRepository: ProjectJpaRepository
) : GitHubMembersScraper {

    private val log by logger()

    override fun scrape(members: PagedIterable<GHUser>, detachedProjectJpa: ProjectJpa) {
        log.debug("Scraping members for ${detachedProjectJpa.projectType}")
        val projectJpa = projectJpaRepository.findOne(detachedProjectJpa.id)
        val currentPlatformUsers = getGitHubUsers(projectJpa)
        val membersIterator = members.iterator()
        while (membersIterator.hasNext()) {
            val member = membersIterator.next()
            val userJpa = member.toUserJpa(GIT_HUB)
            if (currentPlatformUsers.containsKey(userJpa.platformUserId)) {
                projectJpa.users!!.remove(currentPlatformUsers[userJpa.platformUserId])
            }
            projectJpa.users!!.add(userJpa)
        }
        projectJpaRepository.save(projectJpa)
    }

    private fun getGitHubUsers(projectJpa: ProjectJpa) = projectJpa.users!!
            .filter { it.platform.equals(GIT_HUB) }
            .associateBy({ it.platformUserId }, { it })

}
