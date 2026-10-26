package com.example.prak5_2_vysotskiy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prak5_2_vysotskiy.data.User
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val savedUserJson = prefs.getString("USER_DATA", null)
        if (savedUserJson != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(username, password)
            val jsonUser = gson.toJson(user)
            prefs.edit().putString("USER_DATA", jsonUser).apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}