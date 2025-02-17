package com.school.idcard.network

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "my_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_ROLE = "role"
    }

    // Save token
    fun saveToken(token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    // Get token
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun saveRole(token: String) {
        editor.putString(KEY_ROLE, token)
        editor.apply()
    }

    // Get token
    fun getRole(): String? {
        return sharedPreferences.getString(KEY_ROLE, null)
    }

    // Clear data (for logout)
    fun clear() {
        editor.clear()
        editor.apply()
    }




}
