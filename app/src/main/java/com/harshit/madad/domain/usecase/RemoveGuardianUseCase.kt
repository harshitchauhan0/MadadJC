package com.harshit.madad.domain.usecase

import com.harshit.madad.common.Resource
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveGuardianUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(contactItem: ContactItem): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading<Boolean>())
        try {
            profileRepository.removeGuardian(contactItem)
            emit(Resource.Success<Boolean>(true))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }
}