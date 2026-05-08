package com.example.shilpakalaapp.data

import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDao: ImageDao) {

    suspend fun insertUser(user: UserEntity): Long {
        return imageDao.insertUser(user)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity? {
        return imageDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun insertImage(image: ImageEntity) {
        imageDao.insertImage(image)
    }

    fun getImagesWithUser(): Flow<List<ImageWithUser>> {
        return imageDao.getImagesWithUser()
    }

    fun getUsersWithImages(): Flow<List<UserWithImages>> {
        return imageDao.getUsersWithImages()
    }

    suspend fun updateImage(image: ImageEntity) {
        imageDao.updateImage(image)
    }
}
