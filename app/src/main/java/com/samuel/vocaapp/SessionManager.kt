package com.samuel.vocaapp

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private val keyPreference = "login"
    private val keyEmail = "Email"
    private val keyPassword = "Password"
    private val keyGoogleToken = "GoogleToken" // New key for Google token

    init {
        sharedPreferences = context.getSharedPreferences(keyPreference, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    fun sessionLogin(email: String, password: String) {
        editor?.putString(keyEmail, email)
        editor?.putString(keyPassword, password)
        editor?.apply()
    }

    fun sessionLoginWithGoogle(googleToken: String) {
        editor?.putString(keyGoogleToken, googleToken)
        editor?.apply()
    }

    fun sessionLogout() {
        editor?.remove(keyEmail)
        editor?.remove(keyPassword)
        editor?.remove(keyGoogleToken)
        editor?.apply()
    }

    val email: String?
        get() = sharedPreferences?.getString(keyEmail, null)

    val password: String?
        get() = sharedPreferences?.getString(keyPassword, null)

    val googleToken: String?
        get() = sharedPreferences?.getString(keyGoogleToken, null)

    fun isLogin(): Boolean {
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    fun isGoogleLoggedIn(): Boolean {
        return !googleToken.isNullOrEmpty()
    }
}