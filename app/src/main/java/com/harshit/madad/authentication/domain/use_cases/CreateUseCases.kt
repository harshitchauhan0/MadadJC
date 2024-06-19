package com.harshit.madad.authentication.domain.use_cases

import android.util.Log
import com.harshit.madad.authentication.domain.repository.AuthRepository
import com.harshit.madad.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CreateUseCases @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            val result = suspendCoroutine<Resource<String>> { continuation ->
                authRepository.createUser(
                    email = email, password = password,
                    onSignInSuccess = {
                        continuation.resume(Resource.Success<String>("Success"))
                    },
                    onSignInFailed = { exception ->
                        continuation.resume(Resource.Error<String>(exception.message.toString()))
                    }
                )
            }
            emit(result)
        } catch (e: HttpException) {
            emit(Resource.Error<String>(e.message.toString()))
        } catch (e: IOException) {
            emit(Resource.Error<String>(e.message.toString()))
        }
    }
}
