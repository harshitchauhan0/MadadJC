package com.harshit.madad.domain.usecase

import com.harshit.madad.common.Resource
import com.harshit.madad.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveNameUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(name: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading<String>())
        try {
            profileRepository.saveName(name)
            emit(Resource.Success<String>("Success"))
        } catch (e: Exception) {
            emit(Resource.Error<String>(e.message.toString()))
        }
    }
}