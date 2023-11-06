package com.anthonyla.paperize.feature.wallpaper.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    val initialAlbumName: String,
    val folderName: String?,
    val coverUri: String?,
    val wallpapers: List<String> = emptyList(),
    val folderUri: String,
    @PrimaryKey(autoGenerate = false) val key: Int
)