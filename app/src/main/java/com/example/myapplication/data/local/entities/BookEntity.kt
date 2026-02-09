package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,  // "/works/OL12345W"
    val title: String,
    val authors: String?,
    val firstPublishYear: Int?,
    val coverId: Int?,
    val description: String?,
    val subjects: String?
)