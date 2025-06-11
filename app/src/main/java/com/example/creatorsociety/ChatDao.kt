package com.example.creatorsociety

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(chat: ChatEntity)

    @Query("SELECT * FROM chats WHERE communityName = :communityName ORDER BY timestamp ASC")
    suspend fun getChatsForCommunity(communityName: String): List<ChatEntity>

    @Query("DELETE FROM chats WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)
}

