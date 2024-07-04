package com.harshit.madad.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.harshit.madad.domain.repository.AuthRepository
import com.harshit.madad.common.Constants

class AuthRepositoryIMPL(private val application: Application) : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun createUser(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    ) {
        Log.v("TAGG", "Clicked")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess()
            } else {
                onSignInFailed(task.exception!!)
            }
        }
    }

    override fun signIn(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    ) {
        val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sharedPreferences.edit().putBoolean(Constants.LOGGED_IN_KEY, true).apply()
                sharedPreferences.edit().putString(Constants.EMAIL_KEY,email).apply()
                onSignInSuccess()
            } else {
                onSignInFailed(task.exception!!)
            }
        }
    }

    override fun forgetPassword(
        email: String,
        onSignInSuccess: () -> Unit,
        onSignInFailed: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess()
            } else {
                onSignInFailed(task.exception!!)
            }
        }
    }

    override fun checkUserExist(): Boolean {
        val sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constants.LOGGED_IN_KEY, false)
    }
}