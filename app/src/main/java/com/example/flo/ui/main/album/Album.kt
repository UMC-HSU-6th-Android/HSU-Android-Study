package com.example.flo.ui.main.album

import android.media.Image
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null
)

