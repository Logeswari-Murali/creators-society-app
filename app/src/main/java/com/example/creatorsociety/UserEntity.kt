package com.example.creatorsociety

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val username: String,
    val atype: String,
    val bio: String,
    val profileImagePath: String
)
