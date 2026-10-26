package com.example.prak5_2_vysotskiy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.prak5_2_vysotskiy.data.BookData
import com.example.prak5_2_vysotskiy.data.BookEntity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var etIsbn: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnLogout: Button
    private lateinit var btnHistory: Button
    private lateinit var btnSave: Button
    private lateinit var tvResult: TextView
    private var currentBookData: BookData? = null
    private lateinit var currentIsbn: String
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etIsbn = findViewById(R.id.etIsbn)
        btnSearch = findViewById(R.id.btnSearch)
        btnHistory = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogout)
        btnSave = findViewById(R.id.btnSave)
        tvResult = findViewById(R.id.tvResult)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        btnSave.visibility = View.GONE

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnSearch.setOnClickListener {
            val isbn = etIsbn.text.toString().trim()
            if (isbn.isEmpty()) {
                Toast.makeText(this, "Введите ISBN", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentIsbn = isbn
            fetchBookData(isbn)
        }

        btnSave.setOnClickListener {
            val data = currentBookData
            if (data != null) {
                val authorName = if (data.authors != null && data.authors.isNotEmpty()) {
                    data.authors[0].name ?: "Неизвестный автор"
                } else {
                    "Неизвестный автор"
                }
                val bookEntity = BookEntity(
                    isbn = currentIsbn,
                    title = data.title ?: "Без названия",
                    author = authorName,
                    publishDate = data.publish_date ?: "Неизвестно",
                    pages = data.number_of_pages ?: 0
                )
                viewModel.insert(bookEntity)
                Toast.makeText(this, "Книга сохранена!", Toast.LENGTH_SHORT).show()
                btnSave.visibility = View.GONE
            }
        }
    }

    private fun fetchBookData(isbn: String) {
        tvResult.text = "Загрузка..."
        btnSave.visibility = View.GONE

        val url = "https://openlibrary.org/api/books?bibkeys=ISBN:$isbn&format=json&jscmd=data"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val gson = Gson()
                val key = "ISBN:$isbn"
                val bookDataJson = response.optJSONObject(key)

                if (bookDataJson != null) {
                    currentBookData = gson.fromJson(bookDataJson.toString(), BookData::class.java)
                    displayBook(currentBookData!!)
                } else {
                    tvResult.text = "Книга не найдена. Проверьте ISBN."
                }
            } catch (e: Exception) {
                tvResult.text = "Ошибка парсинга JSON"
            }
        }, {
            tvResult.text = "Ошибка сети: ${it.message}"
        })

        Volley.newRequestQueue(this).add(request)
    }

    private fun displayBook(data: BookData) {
        val authorName = if (data.authors != null && data.authors.isNotEmpty()) {
            data.authors[0].name ?: "Неизвестный автор"
        } else {
            "Неизвестный автор"
        }
        val pages = if (data.number_of_pages != null) data.number_of_pages.toString() else "Неизвестно"

        val text = "Название: " + (data.title ?: "Нет данных") + "\n" +
                "Автор: " + authorName + "\n" +
                "Год: " + (data.publish_date ?: "Нет данных") + "\n" +
                "Страниц: " + pages

        tvResult.text = text
        btnSave.visibility = View.VISIBLE
    }
}