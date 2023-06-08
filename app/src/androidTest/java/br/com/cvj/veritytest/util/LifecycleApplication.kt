package br.com.cvj.veritytest.util

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import br.com.cvj.veritytest.MainApplication

class LifecycleApplication : MainApplication(), ActivityLifecycleCallbacks {
    companion object {
        fun getActivity (): Activity? {
            return (InstrumentationRegistry.getInstrumentation()
                .targetContext.applicationContext as? LifecycleApplication)?.currentActivity
        }
    }
    var currentActivity: Activity? = null
        private set

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}