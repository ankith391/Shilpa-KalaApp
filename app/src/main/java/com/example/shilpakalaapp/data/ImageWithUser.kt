package com.example.shilpakalaapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class ImageWithUser(
    @Embedded val image: ImageEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserEntity
)
