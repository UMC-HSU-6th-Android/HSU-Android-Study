package com.example.floClone.core.data.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class SongEntity (
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isRepeating: Boolean = false,
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int? = 0
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}