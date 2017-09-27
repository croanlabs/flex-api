package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.User


fun UserJpa.toUser() =
        User(
                platformUserId = platformUserId,
                platform = platform
        )
