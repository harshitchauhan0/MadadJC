package com.harshit.madad.domain.usecase

import com.harshit.madad.common.Resource
import com.harshit.madad.domain.model.UserInfo
import com.harshit.madad.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserInfoUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke():Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading<UserInfo>())
        try {
            val userInfo = homeRepository.getData()
            emit(Resource.Success<UserInfo>(data = userInfo))
        } catch (e: HttpException) {
            emit(Resource.Error<UserInfo>(message = e.message.toString()))
        } catch (e: IOException) {
            emit(Resource.Error<UserInfo>(message = e.message.toString()))
        }
    }

}