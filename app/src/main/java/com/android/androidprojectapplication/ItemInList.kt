package com.android.androidprojectapplication

data class ItemInList(
    val title: String,
    val imageSrc: Int = 0,
    var isChecked: Boolean = false,
)