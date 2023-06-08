package br.com.cvj.veritytest.util

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import br.com.cvj.veritytest.ui.user.profile.UserProfileActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher


@Suppress("UNCHECKED_CAST", "UNUSED")
abstract class ScreenRobot<T : ScreenRobot<T>> {

    private var activityContext: Activity? = null

    fun checkIsDisplayed(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        return this as T
    }

    fun checkIsHidden(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            Espresso.onView(ViewMatchers.withId(viewId))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, expected: String): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(expected)))
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(messageResId)))
        return this as T
    }

    fun checkViewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withHint(messageResId)))
        return this as T
    }

    fun pressBackButton(): T {
        Espresso.pressBack()
        return this as T
    }


    fun clickOkOnView(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .perform(ViewActions.click())
        return this as T
    }

    fun enterTextIntoView(@IdRes viewId: Int, text: String): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .perform(ViewActions.typeText(text))
        return this as T
    }

    fun provideActivityContext(activityContext: Activity): T {
        this.activityContext = activityContext
        return this as T
    }

    fun checkDialogWithTextIsDisplayed(@StringRes messageResId: Int): T {
        Espresso.onView(ViewMatchers.withText(messageResId))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(activityContext!!.window.decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return this as T
    }

    fun swipeLeftOnView(@IdRes viewId: Int): T {
        Espresso.onView(ViewMatchers.withId(viewId))
            .perform(ViewActions.swipeLeft())
        return this as T
    }

    fun getTextInsideListVH(
        @IdRes recyclerViewId: Int,
        recyclerViewVHMatcher: Matcher<RecyclerView.ViewHolder?>,
        @IdRes textViewId: Int,
    ): String? {
        val text = arrayOfNulls<String>(1)
        val va: ViewAction = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController?, view: View) {
                val descTextView = view.findViewById<TextView>(textViewId)
                text[0] = descTextView.text.toString()
            }
        }

        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .perform(actionOnHolderItem(recyclerViewVHMatcher, va))

        return text[0]
    }

    fun findTextInRecyclerAndTakeActionOnItem(
        @IdRes recyclerViewId: Int,
        text: String,
        viewAction: ViewAction
    ): T {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder?>(
                    ViewMatchers.hasDescendant(
                        ViewMatchers.withText(text)
                    ), viewAction
                )
            )
        return this as T
    }

    fun checkComponentIsVisible(className: String): T {
        Intents.intended(IntentMatchers.hasComponent(className))
        return this as T
    }

    fun scrollToItemAndTakeActionOnRecyclerView(
        @IdRes recyclerViewId: Int,
        matcherIn: Matcher<RecyclerView.ViewHolder?>,
        viewAction: ViewAction
    ): T {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .perform(RecyclerViewActions.scrollToHolder(matcherIn), viewAction)
        return this as T
    }

    fun goToSleep(milliseconds: Long): T {
        Thread.sleep(milliseconds)
        return this as T
    }

    companion object {

        fun <T : ScreenRobot<*>> withRobot(screenRobotClass: Class<T>?): T {
            if (screenRobotClass == null) {
                throw IllegalArgumentException("instance class == null")
            }

            try {
                return screenRobotClass.newInstance()
            } catch (iae: IllegalAccessException) {
                throw RuntimeException("IllegalAccessException", iae)
            } catch (ie: InstantiationException) {
                throw RuntimeException("InstantiationException", ie)
            }

        }
    }
}