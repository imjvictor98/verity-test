package br.com.cvj.veritytest.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


@Suppress("UNUSED")
object MatcherUtil {

    inline fun <reified VH : RecyclerView.ViewHolder> matchInRecyclerView(
        message: String? = null,
        crossinline matches: (item: VH) -> Boolean
    ): Matcher<RecyclerView.ViewHolder?> {
        return object : BoundedMatcher<RecyclerView.ViewHolder?, VH>(VH::class.java) {
            override fun describeTo(description: Description?) {
                message?.let {
                    description?.appendText(it)
                }
            }

            override fun matchesSafely(item: VH) = matches(item)
        }
    }
}