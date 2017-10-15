package ie.reflexivity.flexer.flexapi.extensions

import ie.reflexivity.flexer.flexapi.client.reddit.SubredditAbout
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditPost
import ie.reflexivity.flexer.flexapi.client.reddit.SubredditUser
import ie.reflexivity.flexer.flexapi.db.domain.ProjectJpa
import ie.reflexivity.flexer.flexapi.db.domain.RedditUserData
import ie.reflexivity.flexer.flexapi.db.domain.SubredditJpa
import ie.reflexivity.flexer.flexapi.db.domain.SubredditPostJpa
import ie.reflexivity.flexer.flexapi.db.domain.UserJpa
import ie.reflexivity.flexer.flexapi.model.Platform.REDDIT

fun SubredditAbout.toSubredditJpa(project: ProjectJpa? = null) =
        SubredditJpa(
                project = project,
                display_name = display_name,
                active_user_count = active_user_count,
                accounts_active = accounts_active,
                subscribers = subscribers,
                created = created_utc.toDateTime(),
                redditId = name
        )

fun SubredditPost.toSubredditPostJpa(subreddit: SubredditJpa, author: UserJpa) =
        SubredditPostJpa(
                domain = domain,
                postId = id,
                title = title,
                url = url,
                name = name,
                created = created_utc.toDateTime(),
                view_count = view_count,
                num_comments = num_comments,
                num_crossposts = num_crossposts,
                score = score,
                ups = ups,
                subreddit = subreddit,
                subredditName = name,
                author = author
        )

fun SubredditUser.toUserJpa() =
        UserJpa(
                platform = REDDIT,
                platformUserId = name,
                created = created_utc.toDateTime(),
                platformId = id,
                redditUserData = RedditUserData(
                        link_karma = link_karma,
                        comment_karma = comment_karma
                )
        )
