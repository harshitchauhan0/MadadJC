package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EmailUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            val result = suspendCoroutine<Resource<String>> { continuation ->
                val email = profileRepository.getEmail()
                continuation.resume(Resource.Success<String>(email))
            }
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.message.toString()))
        }
    }
}