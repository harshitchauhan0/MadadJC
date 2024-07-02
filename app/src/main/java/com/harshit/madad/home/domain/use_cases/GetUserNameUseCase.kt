package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            val user = profileRepository.getName()
            emit(Resource.Success<String>(user))
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.message.toString()))
        }
    }
}