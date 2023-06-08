package br.com.cvj.veritytest.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

@Suppress("UNUSED")
class TestRunner : AndroidJUnitRunner() {
    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, LifecycleApplication::class.java.name, context)
    }
}