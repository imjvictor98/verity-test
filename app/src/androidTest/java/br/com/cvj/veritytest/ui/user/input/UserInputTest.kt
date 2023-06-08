package br.com.cvj.veritytest.ui.user.input

import android.content.Context
import android.content.SharedPreferences
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
        getSharedPreferences().edit().clear().apply()
    }

    private fun getSharedPreferences(): SharedPreferences {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.getSharedPreferences("VERITY_STORAGE", Context.MODE_PRIVATE)
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

    //quando o usuário já tem os dados salvos e a tela de perfil aparece
    @Test
    fun on_user_has_account_saved_and_redirected_to_profile() {
        ScreenRobot
            .withRobot(UserInputRobot::class.java)
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