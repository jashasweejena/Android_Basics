package com.example.android_workings.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Photos")
data class PhotoEntity(
    @PrimaryKey val id: Int,
    val albumId: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)