package br.com.cvj.veritytest.ui.user.profile

import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import br.com.cvj.veritytest.base.CFBaseTest
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.util.LocalStorageUtil
import br.com.cvj.veritytest.util.ScreenRobot
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.util.MatcherUtil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserProfileTest: CFBaseTest() {
    companion object {
        private const val USER_INFO_JSON = "{\"login\":\"imjvictor98\",\"id\":47538285,\"node_id\":\"MDQ6VXNlcjQ3NTM4Mjg1\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/47538285?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/imjvictor98\",\"html_url\":\"https://github.com/imjvictor98\",\"followers_url\":\"https://api.github.com/users/imjvictor98/followers\",\"following_url\":\"https://api.github.com/users/imjvictor98/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/imjvictor98/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/imjvictor98/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/imjvictor98/subscriptions\",\"organizations_url\":\"https://api.github.com/users/imjvictor98/orgs\",\"repos_url\":\"https://api.github.com/users/imjvictor98/repos\",\"events_url\":\"https://api.github.com/users/imjvictor98/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/imjvictor98/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"João Victor\",\"company\":\"@mobicare\",\"blog\":\"\",\"location\":\"Rio de Janeiro\",\"email\":null,\"hireable\":null,\"bio\":null,\"twitter_username\":null,\"public_repos\":53,\"public_gists\":0,\"followers\":13,\"following\":10,\"created_at\":\"2019-02-11T19:07:14Z\",\"updated_at\":\"2023-06-05T12:32:48Z\"}"
    }

    @get: Rule
    val userProfileScenario = activityScenarioRule<UserProfileActivity>(
        intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, UserProfileActivity::class.java).apply {
            putExtra(
                "EXTRA_USER_INFO",
                UserInfoResponse(login = "imjvictor98", name = "João Victor")
            )
        }
    )

    @Before
    fun putUserOnStorage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPref = context.getSharedPreferences("VERITY_STORAGE", Context.MODE_PRIVATE)
        sharedPref
            .edit()
            .putString(LocalStorageUtil.Companion.UserInfo.USER_INFO_STORED, USER_INFO_JSON)
            .apply()
    }

    @Test
    fun when_user_has_repositories() {
        userProfileScenario.scenario

        ScreenRobot
            .withRobot(UserProfileRobot::class.java)
            .verifyNameOnList("android-jetpack-compose")

    }

    class UserProfileRobot: ScreenRobot<UserProfileRobot>() {
        fun verifyNameOnList(repoName: String): UserProfileRobot {
            val resultName = this.getTextInsideListVH(
                R.id.user_profile_repos_list,
                MatcherUtil.matchInRecyclerView<UserProfileAdapter.ViewHolder> { item  ->
                    item.binding.listItemUserProfileName.text.contentEquals(repoName, true)
                },
                R.id.list_item_user_profile_name
            )

            assertEquals(repoName, resultName)
            return this
        }
    }
}

class UserProfileNoReposTest: CFBaseTest() {
    @get: Rule
    val userProfileScenario = activityScenarioRule<UserProfileActivity>(
        intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, UserProfileActivity::class.java).apply {
            putExtra(
                "EXTRA_USER_INFO",
                UserInfoResponse(login = "oia", name = "oia")
            )
        }
    )

    @Test
    fun when_user_has_no_repositories() {
        ScreenRobot
            .withRobot(UserProfileTest.UserProfileRobot::class.java)
            .checkIsHidden(R.id.user_profile_repos_list)
    }
}