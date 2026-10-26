package com.example.prak5_2_vysotskiy

import androidx.room.*
import com.example.prak5_2_vysotskiy.data.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Query("SELECT * FROM books_table ORDER BY id DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Delete
    suspend fun delete(book: BookEntity)

    @Update
    suspend fun update(book: BookEntity)
}