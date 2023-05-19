package egorka.artemiyev.naildesignconstructor.app

import android.content.Context

class DataManager(private val baseContext: Context) {
    private val preferences =
        baseContext.getSharedPreferences("NailDesignConstructor", Context.MODE_PRIVATE)

    fun passLogin() {
        preferences.edit().putBoolean("isLogin", true).apply()
    }

    fun logout() {
        preferences.edit().putBoolean("isLogin", false).apply()
    }

    fun isLoginPassed(): Boolean {
        return preferences.getBoolean("isLogin", false)
    }
}