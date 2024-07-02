package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetGuardianListUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
     operator fun invoke(): Flow<Resource<List<ContactItem>>> = flow {
        emit(Resource.Loading<List<ContactItem>>())
        try {
            val result = profileRepository.guardianList()
            emit(Resource.Success(result))

        } catch (e: HttpException) {
            emit(Resource.Error<List<ContactItem>>(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resource.Error<List<ContactItem>>(message = e.message.toString()))
        }
    }

}