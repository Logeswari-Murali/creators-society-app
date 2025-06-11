package com.example.creatorsociety

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "communities")
data class CommunityEntity(
    @PrimaryKey val communityId: String,
    val communityName: String,
    val userId: String  // Foreign key (not enforced in Room)
)
