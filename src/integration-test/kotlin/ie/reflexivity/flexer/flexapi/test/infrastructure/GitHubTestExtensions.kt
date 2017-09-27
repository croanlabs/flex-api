package ie.reflexivity.flexer.flexapi.test.infrastructure

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.extensions.toDate
import org.kohsuke.github.GHUser


fun UserJpa.toMockedGHUser(): GHUser {
    val user = mock<GHUser>()
    whenever(user.email).thenReturn(email)
    whenever(user.location).thenReturn(location)
    whenever(user.blog).thenReturn(blog)
    whenever(user.company).thenReturn(company)
    whenever(user.followersCount).thenReturn(gitHubFollowersCount)
    whenever(user.followingCount).thenReturn(gitHubFollowingCount)
    whenever(user.id).thenReturn(platformId!!.toInt())
    whenever(user.createdAt).thenReturn(created?.toDate())
    whenever(user.login).thenReturn(platformUserId)
    whenever(user.name).thenReturn(name)
    whenever(user.id).thenReturn(platformId?.toInt())
    return user
}
