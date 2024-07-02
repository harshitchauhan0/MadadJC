package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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