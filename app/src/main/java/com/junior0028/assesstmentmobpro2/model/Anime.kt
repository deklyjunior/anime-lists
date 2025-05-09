package com.junior0028.assesstmentmobpro2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class Anime(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val genre: String,
    val rating: Int,
    val season: String,
    val episodeCount: Int = 0,
    val description: String = "",
    val isDeleted: Boolean = false,
    val deletedDate: Long = 0L,
    val isWatched: Boolean,
    val deletedAt: Long
)