package com.harshit.madad.domain.usecase

import com.harshit.madad.common.Resource
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.GuardianRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveGuardianListUseCase @Inject constructor(private val guardianRepository: GuardianRepository) {
    operator fun invoke(guardianList: List<ContactItem>): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading<Boolean>())
        try {
            guardianRepository.saveGuardianList(guardianList)
            emit(Resource.Success<Boolean>(data = true))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(message = e.message.toString()))
        }
    }
}