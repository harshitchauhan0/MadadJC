package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGuardianListUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(): Flow<Resource<List<ContactItem>>> = flow{
        emit(Resource.Loading<List<ContactItem>>())
        try {
            val guardianList = profileRepository.guardianList()
            emit(Resource.Success<List<ContactItem>>(guardianList))
        }catch (e: Exception){
            emit(Resource.Error<List<ContactItem>>(e.message.toString()))
        }
    }
}