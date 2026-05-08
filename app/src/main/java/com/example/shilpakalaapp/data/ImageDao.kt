package com.example.shilpakalaapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    // User Auth
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    // Image Upload
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageEntity)

    // Browsing Gallery (All images + artist info)
    @Transaction
    @Query("SELECT * FROM images ORDER BY id DESC")
    fun getImagesWithUser(): Flow<List<ImageWithUser>>

    // Dashboard (All users + their images)
    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersWithImages(): Flow<List<UserWithImages>>

    @Update
    suspend fun updateImage(image: ImageEntity)
}
