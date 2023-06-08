package br.com.cvj.veritytest.util

import androidx.test.espresso.idling.CountingIdlingResource
import timber.log.Timber

object CustomIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE, true)
    var counter = 0

    fun increment(where: Any) {
        counter.plus(1)
        countingIdlingResource.increment()
        printDebugMessage("async --> ${where::class.java.name}")
    }

    fun decrement(where: Any) {
        if (!countingIdlingResource.isIdleNow) {
            if (counter > 0) counter.minus(1)
            countingIdlingResource.decrement()
            printDebugMessage("async <-- ${where::class.java.name}")
        }
    }

    fun decrementAll(where: Any) {
        if (!countingIdlingResource.isIdleNow) {
            for (i in counter downTo 0 step 1) {
                decrement(where::class.java.name)
            }
            counter = 0
        }
    }

    private fun printDebugMessage(debugMessage: String? = null) {
        if (debugMessage != null) {
            Timber.tag("CountingIdlingResource").d(debugMessage)
        }
    }
}