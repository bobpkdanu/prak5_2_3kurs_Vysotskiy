package com.example.prak5_2_vysotskiy.data

data class BookData(
    val title: String?,
    val publish_date: String?,
    val number_of_pages: Int?,
    val authors: List<AuthorData>?
)

data class AuthorData(
    val name: String?
)