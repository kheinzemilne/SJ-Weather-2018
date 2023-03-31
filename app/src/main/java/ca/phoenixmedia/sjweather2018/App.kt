package ca.phoenixmedia.sjweather2018

import android.app.Application
import android.content.Context

// I always have one of these in order to have a context available wherever it's needed.
// This particular type of global context is not vulnerable to being leaked because it is tied
// to the overall app lifecycle.
class App : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}