package com.example.flo_clone.data

data class Song (
    val title: String = "",
    val singer: String = "",
    var second: Int = 0,
    val playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isRepeating: Boolean = false
)