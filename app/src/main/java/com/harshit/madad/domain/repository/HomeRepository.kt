package com.harshit.madad.domain.repository

import com.harshit.madad.domain.model.UserInfo

interface HomeRepository {
    fun logout():Boolean

    fun getData(): UserInfo
}