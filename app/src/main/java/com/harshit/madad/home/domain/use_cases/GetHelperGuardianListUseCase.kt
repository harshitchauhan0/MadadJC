package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHelperGuardianListUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    operator fun invoke(): Flow<Resource<List<ContactItem>>> = flow {
        emit(Resource.Loading<List<ContactItem>>())
        try {
           val result = messageRepository.guardianList()
           emit(Resource.Success<List<ContactItem>>(result))
        } catch (e: Exception) {
            emit(Resource.Error<List<ContactItem>>(e.message.toString()))
        }
    }
}