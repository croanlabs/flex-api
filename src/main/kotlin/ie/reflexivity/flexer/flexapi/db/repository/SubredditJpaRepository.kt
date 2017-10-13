package ie.reflexivity.flexer.flexapi.db.repository

import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubredditJpaRepository : JpaRepository<SubredditJpa, Long> {

    fun findByRedditId(redditId: String): SubredditJpa?

}
