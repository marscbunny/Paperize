package com.anthonyla.paperize.feature.wallpaper.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Album model
 *
 * @param initialAlbumName The initial album name -- should not be changed as it is used as the key for database queries
 * @param displayedAlbumName The displayed album name
 * @param coverUri The cover uri of the album
 */
@Entity
data class Album(
    @PrimaryKey(autoGenerate = false) val initialAlbumName: String,
    val displayedAlbumName: String,
    val coverUri: String?,
)