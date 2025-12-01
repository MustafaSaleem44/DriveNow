package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_login)

        val loginButton = findViewById<TextView>(R.id.text_login)
        loginButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardOverview::class.java)
            startActivity(intent)
        }
    }
}