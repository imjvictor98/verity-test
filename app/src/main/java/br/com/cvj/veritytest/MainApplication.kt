package br.com.cvj.veritytest

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber


@Suppress("UNUSED")
open class MainApplication : Application() {
    companion object {
        private lateinit var sInstance: MainApplication
        fun getInstance() = sInstance
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sInstance = this
        setupTimber()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}