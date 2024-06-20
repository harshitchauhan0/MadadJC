package com.harshit.madad.authentication.domain.use_cases

import com.harshit.madad.authentication.domain.repository.AuthRepository
import com.harshit.madad.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CheckLoggedInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            val result = suspendCoroutine<Resource<String>> { continuation ->
                val user = authRepository.checkUserExist()
                if (user) {
                    continuation.resume(Resource.Success<String>("Success"))
                } else {
                    continuation.resume(Resource.Error<String>("User not found"))
                }
            }
            emit(result)
        } catch (e: HttpException) {
            emit(Resource.Error<String>(e.message.toString()))
        } catch (e: IOException) {
            emit(Resource.Error<String>(e.message.toString()))
        }
    }
}