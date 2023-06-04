package egorka.artemiyev.naildesignconstructor.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.MyDesign
import egorka.artemiyev.naildesignconstructor.repository.Api

class DataManager(private val baseContext: Context) {
    private val preferences =
        baseContext.getSharedPreferences("NailDesignConstructor", Context.MODE_PRIVATE)

    val api = Api.createApi()

    fun passLogin() {
        preferences.edit().putBoolean("isLogin", true).apply()
    }

    fun logout() {
        preferences.edit().putBoolean("isLogin", false).apply()
    }

    fun isLoginPassed(): Boolean {
        return preferences.getBoolean("isLogin", false)
    }

    fun getUserKey(): String {
        return preferences.getString("UserKey", "") ?: ""
    }

    fun setUserKey(key: String) {
        return preferences.edit().putString("UserKey", key).apply()
    }

    fun isFirstLaunch(): Boolean {
        return preferences.getBoolean("isFirstLaunch", true)
    }

    fun endFirstLaunch() {
        return preferences.edit().putBoolean("isFirstLaunch", false).apply()
    }

    fun setListFavorite(list: String) {
        return preferences.edit().putString("favoriteList${getUserKey()}", list).apply()
    }

    fun getListFavorite(): MasterWorkList {
        return try {
            Gson().fromJson(
                preferences.getString("favoriteList${getUserKey()}", "") ?: "",
                object : TypeToken<MasterWorkList>() {}.type
            )
        } catch (e: Exception) {
            MasterWorkList()
        }
    }

    fun addListMy(list: String) {
        return preferences.edit().putString("myList${getUserKey()}", list).apply()
    }

    fun getListMy(): MutableList<MyDesign> {
        return try {
            Gson().fromJson(
                preferences.getString("myList${getUserKey()}", "") ?: "",
                object : TypeToken<MutableList<MyDesign>>() {}.type
            )
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}