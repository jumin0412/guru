package com.example.guru

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {
    lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupText = findViewById<TextView>(R.id.signupText)

        dbHelper = UserDatabaseHelper(this)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim().lowercase()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.checkUser(email, password)
            if (success) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RealMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "이메일 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
            }
        }

        signupText.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }
}