package ie.reflexivity.flexer.flexapi.db.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
data class UserJpa(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        val id: Long = 0,

        @field:[NotNull Size(min = 1, max = 50)]
        @Column(length = 50, unique = true, nullable = false)
        val userId: String,

        @field:[NotNull Size(min = 1, max = 60)]
        @Column(name = "password_hash", length = 60)
        val password: String
)
