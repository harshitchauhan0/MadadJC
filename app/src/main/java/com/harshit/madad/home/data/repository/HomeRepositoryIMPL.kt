package com.harshit.madad.home.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.harshit.madad.common.Constants
import com.harshit.madad.home.data.remote.dto.UserInfo
import com.harshit.madad.home.domain.repository.HomeRepository

class HomeRepositoryIMPL(private val application: Application) : HomeRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun logout(): Boolean {
        try {
            auth.signOut()
            val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(Constants.LOGGED_IN_KEY,false).apply()
            return true
        } catch (e: Exception) {
            Log.v("TAG", e.message.toString())
        }
        return false
    }

    override fun getData(): UserInfo {
        val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.NAME_KEY, "")
        val phone = sharedPreferences.getString(Constants.PHONE_KEY, "")
        return UserInfo(name ?: "", phone ?: "")
    }
}