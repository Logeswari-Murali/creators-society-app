package com.example.creatorsociety

import androidx.room.*

@Dao
interface CommunityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: CommunityEntity)

    @Query("SELECT * FROM communities WHERE userId = :userId")
    suspend fun getCommunities(userId: String): List<CommunityEntity>
}
