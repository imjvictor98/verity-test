package br.com.cvj.veritytest.util

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

@Suppress("UNUSED")
object ActionUtil {
    fun getTextFromTextView(matcher: ViewInteraction): String? {
        val text = arrayOfNulls<String>(1)
        val va: ViewAction = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController?, view: View) {
                val tv = view as TextView
                text[0] = tv.text.toString()
            }
        }
        matcher.perform(va)
        return text[0]
    }
}