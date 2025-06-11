package com.example.creatorsociety

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUser(id: String): UserEntity?

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
