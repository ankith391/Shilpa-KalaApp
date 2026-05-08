package com.example.shilpakalaapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithImages(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val images: List<ImageEntity>
)
