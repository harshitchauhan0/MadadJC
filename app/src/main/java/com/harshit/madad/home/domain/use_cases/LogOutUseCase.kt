package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LogOutUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            val result = suspendCoroutine<Resource<String>> { continuation ->
                val user = homeRepository.logout()
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