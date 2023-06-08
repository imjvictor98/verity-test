package br.com.cvj.veritytest.util

import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.MoshiWrapper

open class MoshiUtil {
    companion object {
        const val USER_INFO_SUCCESS_JSON = "{\"login\":\"imjvictor98\",\"id\":47538285,\"node_id\":\"MDQ6VXNlcjQ3NTM4Mjg1\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/47538285?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/imjvictor98\",\"html_url\":\"https://github.com/imjvictor98\",\"followers_url\":\"https://api.github.com/users/imjvictor98/followers\",\"following_url\":\"https://api.github.com/users/imjvictor98/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/imjvictor98/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/imjvictor98/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/imjvictor98/subscriptions\",\"organizations_url\":\"https://api.github.com/users/imjvictor98/orgs\",\"repos_url\":\"https://api.github.com/users/imjvictor98/repos\",\"events_url\":\"https://api.github.com/users/imjvictor98/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/imjvictor98/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"João Victor\",\"company\":\"@mobicare\",\"blog\":\"\",\"location\":\"Rio de Janeiro\",\"email\":null,\"bio\":null,\"twitter_username\":null,\"public_repos\":53,\"public_gists\":0,\"followers\":13,\"following\":10,\"created_at\":\"2019-02-11T19:07:14Z\",\"updated_at\":\"2023-06-05T12:32:48Z\"}"
    }

    inline fun <reified T> fromJson(json: String): T = MoshiWrapper.moshi
        .adapter(T::class.java)
        .fromJson(json)!!

    inline fun <reified T> toJson(obj: T): String = MoshiWrapper.moshi
        .adapter(T::class.java)
        .toJson(obj)


    val userInfoFromJson = fromJson<UserInfoResponse>(USER_INFO_SUCCESS_JSON)
    val userInfoAsJson = USER_INFO_SUCCESS_JSON
}