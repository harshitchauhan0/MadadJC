package com.harshit.madad.authentication.presentation.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshit.madad.authentication.domain.use_cases.CheckLoggedInUseCase
import com.harshit.madad.authentication.domain.use_cases.CreateUseCases
import com.harshit.madad.authentication.domain.use_cases.ForgetPasswordUseCase
import com.harshit.madad.authentication.domain.use_cases.SignInUseCase
import com.harshit.madad.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val createUseCases: CreateUseCases,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val checkLoggedInUseCase: CheckLoggedInUseCase
) : ViewModel() {

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    init {
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        viewModelScope.launch {
            checkLoggedInUseCase.invoke().onEach { result->
                when(result){
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        _navigateToHome.value = true
                    }
                }
            }.launchIn(this)
        }
    }

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _passWord = mutableStateOf("")
    val passWord: State<String> = _passWord

    private val _createState = mutableStateOf(CreateAccountState())
    val createState: State<CreateAccountState> = _createState

    private val _signInState = mutableStateOf(SignInState())
    val signInState: State<SignInState> = _signInState

    private val _showForgotPassword = mutableStateOf(false)
    val showForgotPasswordState: State<Boolean> = _showForgotPassword

    private val _forgetPasswordMail = mutableStateOf("")
    val forgetPasswordMail: State<String> = _forgetPasswordMail

    private val _forgetPasswordState = mutableStateOf(ForgetPasswordState())
    val forgetPasswordState: State<ForgetPasswordState> = _forgetPasswordState

    fun onCreateAccount() {
        val email = email.value
        val password = passWord.value

        if (!isValidEmail(email)) {
            _createState.value = CreateAccountState(error = "Invalid email")
            return
        }

        if (!isValidPassword(password)) {
            _createState.value = CreateAccountState(error = "Invalid password")
            return
        }

        viewModelScope.launch {
            createUseCases(email, password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _createState.value = CreateAccountState(isAccountCreated = true)
                    }

                    is Resource.Error -> {
                        _createState.value =
                            CreateAccountState(error = result.message ?: "Unknown Error")
                    }

                    is Resource.Loading -> {
                        _createState.value = CreateAccountState(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    fun onSignAccount() {
        val email = email.value
        val password = passWord.value

        if (!isValidEmail(email)) {
            _createState.value = CreateAccountState(error = "Invalid email")
            return
        }

        if (!isValidPassword(password)) {
            _createState.value = CreateAccountState(error = "Invalid password")
            return
        }
        viewModelScope.launch {
            signInUseCase(email, password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _signInState.value = SignInState(isSignedIn = true)
                    }

                    is Resource.Error -> {
                        _signInState.value = SignInState(error = result.message ?: "Unknown Error")
                    }

                    is Resource.Loading -> {
                        _signInState.value = SignInState(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() }
    }

    fun onForgetPassWord() {
        val email = forgetPasswordMail.value

        if (!isValidEmail(email)) {
            _forgetPasswordState.value = ForgetPasswordState(error = "Invalid email")
            return
        }
        Log.v("CLICK","CLICL")
        viewModelScope.launch {
            forgetPasswordUseCase(email).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _forgetPasswordState.value = ForgetPasswordState(isForgetPassword = true)
                    }

                    is Resource.Error -> {
                        _forgetPasswordState.value = ForgetPasswordState(error = result.message ?: "Unknown Error")
                    }

                    is Resource.Loading -> {
                        _forgetPasswordState.value = ForgetPasswordState(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    fun showForgotPassword(value: Boolean) {
        _showForgotPassword.value = value
        _forgetPasswordMail.value = email.value
    }

    fun forgetPasswordMailChange(newMail: String) {
        _forgetPasswordMail.value = newMail
    }

    fun emailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun passwordChange(newPassword: String) {
        _passWord.value = newPassword
    }
}