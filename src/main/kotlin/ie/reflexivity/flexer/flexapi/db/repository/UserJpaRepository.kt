package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, Long> {

    fun findByPlatformUserIdAndPlatform(platformUserId: String, platform: Platform): UserJpa?

    fun findByPlatform(platform: Platform, pageable: Pageable): Page<UserJpa>
}
