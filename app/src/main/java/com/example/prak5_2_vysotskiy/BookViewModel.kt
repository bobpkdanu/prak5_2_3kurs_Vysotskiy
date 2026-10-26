package com.example.prak5_2_vysotskiy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prak5_2_vysotskiy.data.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookDao()
    val allBooks: Flow<List<BookEntity>> = dao.getAllBooks()

    fun insert(book: BookEntity) = viewModelScope.launch { dao.insert(book) }
    fun delete(book: BookEntity) = viewModelScope.launch { dao.delete(book) }
    fun update(book: BookEntity) = viewModelScope.launch { dao.update(book) }
}