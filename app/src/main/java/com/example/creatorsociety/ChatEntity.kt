package com.example.creatorsociety

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val communityName: String, // Identifies the community

    @ColumnInfo(name = "sender_id")
    val senderId: String, // String to store Firebase UID

    val message: String?, // Text message
    val imageUri: String?, // Image message (if any)
    val timestamp: Long // Time of the message
)
