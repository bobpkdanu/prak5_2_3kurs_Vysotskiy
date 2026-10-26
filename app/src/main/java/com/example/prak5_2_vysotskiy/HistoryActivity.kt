package com.example.prak5_2_vysotskiy

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prak5_2_vysotskiy.data.BookEntity
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var booksContainer: LinearLayout
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)
        booksContainer = LinearLayout(this)
        booksContainer.orientation = LinearLayout.VERTICAL
        booksContainer.setPadding(32, 32, 32, 32)
        booksContainer.setBackgroundColor(Color.parseColor("#A0522D"))
        scrollView.addView(booksContainer)
        setContentView(scrollView)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        lifecycleScope.launch {
            viewModel.allBooks.collect { books ->
                renderBooks(books)
            }
        }
    }

    private fun renderBooks(books: List<BookEntity>) {
        booksContainer.removeAllViews()

        val title = TextView(this)
        title.text = "Сохранённые книги"
        title.textSize = 24f
        title.setTextColor(Color.WHITE)
        title.typeface = Typeface.DEFAULT_BOLD
        title.setPadding(0, 0, 0, 32)
        booksContainer.addView(title)

        if (books.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "Список пуст, Сохраните первую книгу"
            tvEmpty.textSize = 16f
            tvEmpty.setTextColor(Color.GRAY)
            tvEmpty.gravity = Gravity.CENTER
            tvEmpty.setPadding(0, 80, 0, 0)
            booksContainer.addView(tvEmpty)
        }

        for (book in books) {
            booksContainer.addView(createBookItem(book))
        }
    }

    private fun createBookItem(book: BookEntity): LinearLayout {
        val layout = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 24)
        layout.layoutParams = params
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)
        layout.setBackgroundColor(Color.parseColor("#F0F0F0"))

        val tvTitle = TextView(this)
        tvTitle.text = book.title
        tvTitle.textSize = 18f
        tvTitle.setTextColor(Color.BLACK)
        tvTitle.typeface = Typeface.DEFAULT_BOLD
        layout.addView(tvTitle)

        val tvDetails = TextView(this)
        tvDetails.text = "Автор: ${book.author}" +
                "\nГод: ${book.publishDate}" +
                "\nСтраниц: ${book.pages}" +
                "\nISBN: ${book.isbn}"
        tvDetails.textSize = 14f
        tvDetails.setTextColor(Color.parseColor("#555555"))
        tvDetails.setPadding(0, 12, 0, 24)
        layout.addView(tvDetails)

        val etNote = EditText(this)
        etNote.hint = "Заметка о книге"
        etNote.setText(book.note)
        etNote.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        etNote.minLines = 2
        etNote.setPadding(24, 24, 24, 24)
        etNote.setBackgroundColor(Color.WHITE)
        etNote.setTextColor(Color.BLACK)
        etNote.setTextSize(16f)
        layout.addView(etNote)

        val buttonsLayout = LinearLayout(this)
        buttonsLayout.orientation = LinearLayout.HORIZONTAL
        buttonsLayout.setPadding(0, 16, 0, 0)

        val btnUpdate = Button(this)
        btnUpdate.text = "Обновить"
        val btnParams1 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        btnParams1.setMargins(0, 0, 16, 0)
        btnUpdate.layoutParams = btnParams1
        btnUpdate.setOnClickListener {
            book.note = etNote.text.toString()
            viewModel.update(book)
            Toast.makeText(this, "Заметка обновлена", Toast.LENGTH_SHORT).show()
        }

        val btnDelete = Button(this)
        btnDelete.text = "Удалить"
        val btnParams2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        btnDelete.layoutParams = btnParams2
        btnDelete.setBackgroundColor(Color.parseColor("#E53935"))
        btnDelete.setTextColor(Color.WHITE)
        btnDelete.setOnClickListener {
            viewModel.delete(book)
            Toast.makeText(this, "Книга удалена", Toast.LENGTH_SHORT).show()
        }

        buttonsLayout.addView(btnUpdate)
        buttonsLayout.addView(btnDelete)
        layout.addView(buttonsLayout)

        return layout
    }
}