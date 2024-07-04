package com.harshit.madad.domain.usecase

import com.harshit.madad.common.Resource
import com.harshit.madad.domain.model.ContactItem
import com.harshit.madad.domain.repository.GuardianRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val guardianRepository: GuardianRepository) {
    operator fun invoke(): Flow<Resource<List<ContactItem>>> = flow {
        emit(Resource.Loading<List<ContactItem>>())
        try {
            val contacts = guardianRepository.getContacts()
            emit(Resource.Success<List<ContactItem>>(data = contacts))
        } catch (e: Exception) {
            emit(Resource.Error<List<ContactItem>>(message = e.message.toString()))
        }
    }
}