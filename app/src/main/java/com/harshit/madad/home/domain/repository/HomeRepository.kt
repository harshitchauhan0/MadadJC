package com.harshit.madad.home.domain.repository

import com.harshit.madad.home.data.remote.dto.UserInfo

interface HomeRepository {
    fun logout():Boolean

    fun getData(): UserInfo
}