package br.com.cvj.veritytest.ui.user.input

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.base.CFBaseTest
import br.com.cvj.veritytest.ui.user.profile.UserProfileActivity
import br.com.cvj.veritytest.util.ScreenRobot
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class UserInputTest: CFBaseTest() {
    companion object {
        private const val USERNAME_TO_BE_TYPED = "imjvictor98"
        @BeforeClass
        fun clearCacheThroughAdb() {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("pm clear br.com.cvj.veritytest").close()
        }
    }

    @get: Rule
    val userInputScenario = activityScenarioRule<UserInputActivity>()

    override fun start() {
        clearSharedPreferences()
        super.start()
    }

    private fun clearSharedPreferences() {
        getSharedPreferences().edit().clear().apply()
    }

    private fun getSharedPreferences(): SharedPreferences {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.getSharedPreferences("VERITY_STORAGE", Context.MODE_PRIVATE)
    }

    @Test
    fun on_user_not_put_username_then_click_in_continue_btn_error_message_appears() {
        userInputScenario.scenario

        ScreenRobot
            .withRobot(UserInputRobot::class.java)
            .clickOnContinueButton()
            .checkIsDisplayed(R.id.custom_dialog_horizontal_container)
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