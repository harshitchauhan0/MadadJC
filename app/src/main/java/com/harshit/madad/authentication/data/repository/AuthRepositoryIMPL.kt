package com.harshit.madad.authentication.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.harshit.madad.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryIMPL : AuthRepository {
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
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
        return FirebaseAuth.getInstance().currentUser != null
    }
}