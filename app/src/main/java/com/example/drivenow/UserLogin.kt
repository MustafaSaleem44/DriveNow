package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val loginButton = findViewById<TextView>(R.id.text_login)
        loginButton.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val companyLoginButton = findViewById<TextView>(R.id.text_company)
        companyLoginButton.setOnClickListener {
            val intent = Intent(this, CompanyLogin::class.java)
            startActivity(intent)
        }
    }
}