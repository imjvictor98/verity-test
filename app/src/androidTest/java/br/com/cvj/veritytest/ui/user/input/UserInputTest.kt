package br.com.cvj.veritytest.ui.user.input

import android.content.Context
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import br.com.cvj.veritytest.R
import br.com.cvj.veritytest.base.CFBaseTest
import br.com.cvj.veritytest.ui.user.profile.UserProfileActivity
import br.com.cvj.veritytest.util.ScreenRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserInputTest: CFBaseTest() {
    companion object {
        private const val USERNAME_TO_BE_TYPED = "imjvictor98"
        private const val USERNAME_NONEXISTENT_TO_BE_TYPED = "OIA"
        private const val DIALOG_EXPECT_POPUP = 2000L
    }

    @get: Rule
    val userInputScenario = activityScenarioRule<UserInputActivity>()

    @Before
    fun clearSharedPreferences() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("VERITY_STORAGE", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    @Test
    fun on_user_not_put_username_then_click_in_continue_btn_error_message_appears() {
        userInputScenario.scenario

        ScreenRobot
            .withRobot(UserInputRobot::class.java)
            .clickOnContinueButton()
            .checkViewHasText(R.id.custom_dialog_sub_title, R.string.user_input_empty_dialog_description)
    }

    @Test
    fun on_user_type_nonexistent_user_and_dialog_popup_with_not_found_user_message() {
        ScreenRobot
            .withRobot(UserInputRobot::class.java)
            .inputUsername(USERNAME_NONEXISTENT_TO_BE_TYPED)
            .clickOnContinueButton()
            .goToSleep(DIALOG_EXPECT_POPUP)
            .checkViewHasText(R.id.custom_dialog_sub_title, R.string.user_input_not_exists_dialog_description)
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