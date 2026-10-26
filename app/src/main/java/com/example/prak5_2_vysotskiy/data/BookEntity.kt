package com.example.prak5_2_vysotskiy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isbn: String,
    val title: String,
    val author: String,
    val publishDate: String,
    val pages: Int,
    var note: String = ""
)