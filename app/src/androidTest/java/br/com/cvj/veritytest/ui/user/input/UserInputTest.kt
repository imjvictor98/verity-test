package br.com.cvj.veritytest.ui.user.input

import android.content.Context
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.base.CFBaseTest
import br.com.cvj.veritytest.ui.user.profile.UserProfileActivity
import br.com.cvj.veritytest.util.ScreenRobot
import org.junit.Rule
import org.junit.Test

class UserInputTest: CFBaseTest() {
    companion object {
        private const val USERNAME_TO_BE_TYPED = "imjvictor98"
    }

    @get: Rule
    val userInputScenario = activityScenarioRule<UserInputActivity>()

    override fun start() {
        clearSharedPreferences()
        super.start()
    }

    private fun clearSharedPreferences() {
        val context =  InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPrefs = context.getSharedPreferences("VERITY_STORAGE", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }

    @Test
    fun on_user_do_not_have_github_account_saved() {
        userInputScenario.scenario

        ScreenRobot
            .withRobot(UserInputRobot::class.java)
            .inputUsername(USERNAME_TO_BE_TYPED)
            .clickOnContinueButton()
            .ensureProfileIsVisible()
    }


    class UserInputRobot: ScreenRobot<UserInputRobot>() {
        fun inputUsername(username: String): UserInputRobot {
            return enterTextIntoView(R.id.user_input_et, username)
        }

        fun clickOnContinueButton(): UserInputRobot {
            return clickOkOnView(R.id.user_input_btn)
        }

        fun ensureProfileIsVisible(): UserInputRobot {
            return checkComponentIsVisible(UserProfileActivity::class.java.name)
        }
    }
}