package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.SubredditPostJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubredditPostJpaRepository : JpaRepository<SubredditPostJpa, Long> {

    fun findByName(name: String): SubredditPostJpa?

}
