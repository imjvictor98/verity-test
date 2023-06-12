package br.com.cvj.veritytest.util

import androidx.test.espresso.idling.CountingIdlingResource
import timber.log.Timber

object CustomIdlingResource {
    private const val RESOURCE = "GLOBAL"
    //Ativar para instrumented tests
    private var isEnable = true

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE, true)
    private var counter = 0

    fun increment(where: Any) {
        if (isEnable) {
            counter.plus(1)
            countingIdlingResource.increment()
            printDebugMessage("async --> ${where::class.java.name}")
        }
    }

    fun decrement(where: Any) {
        if (!countingIdlingResource.isIdleNow && isEnable) {
            if (counter > 0) counter.minus(1)
            countingIdlingResource.decrement()
            printDebugMessage("async <-- ${where::class.java.name}")
        }
    }

    fun decrementAll(where: Any) {
        if (!countingIdlingResource.isIdleNow && isEnable) {
            for (i in counter downTo 0 step 1) {
                decrement(where::class.java.name)
            }
            counter = 0
        }
    }

    private fun printDebugMessage(debugMessage: String? = null) {
        if (debugMessage != null && isEnable) {
            Timber.tag("CountingIdlingResource").d(debugMessage)
        }
    }
}