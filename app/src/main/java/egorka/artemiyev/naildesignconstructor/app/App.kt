package egorka.artemiyev.naildesignconstructor.app

import android.app.Application

class App: Application() {
    companion object {
        lateinit var dm: DataManager
    }

    override fun onCreate() {
        super.onCreate()
        dm = DataManager(baseContext)
    }
}