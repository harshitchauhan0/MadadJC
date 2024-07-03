package com.harshit.madad.home.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ContactItem(
    @PrimaryKey val id: Int? = null,
    val name: String = "",
    val phoneNumber: String = "",
    var isSelected: Boolean = false,
    var isSuperGuardian: Boolean = false,
    val key: String = UUID.randomUUID().toString()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactItem

        if (key != other.key) return false
        if (isSelected != other.isSelected) return false
        if (isSuperGuardian != other.isSuperGuardian) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + isSelected.hashCode()
        result = 31 * result + isSuperGuardian.hashCode()
        return result
    }
}
