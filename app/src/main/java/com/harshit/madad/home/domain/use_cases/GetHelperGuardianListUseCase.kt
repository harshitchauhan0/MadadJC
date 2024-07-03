package com.harshit.madad.home.domain.use_cases

import com.harshit.madad.common.Resource
import com.harshit.madad.home.data.remote.dto.ContactItem
import com.harshit.madad.home.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHelperGuardianListUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    operator fun invoke(): Flow<Resource<List<ContactItem>>> = flow {
        emit(Resource.Loading<List<ContactItem>>())
        try {
            val result = messageRepository.guardianList()
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error<List<ContactItem>>(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resource.Error<List<ContactItem>>(message = e.message.toString()))
        }
    }
}