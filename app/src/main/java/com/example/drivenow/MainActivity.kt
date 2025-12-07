package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.drivenow.utils.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is already logged in
        val prefsHelper = SharedPreferencesHelper(this)
        if (prefsHelper.isLoggedIn()) {
            val userType = prefsHelper.getUserType()
            val intent = if (userType == "COMPANY") {
                Intent(this, CompanyDashboardOverview::class.java)
            } else {
                Intent(this, MainPage::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }
        
        setContentView(R.layout.activity_main)

        val getStartedButton = findViewById<FrameLayout>(R.id.button_get_started)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }

        val skipButton = findViewById<LinearLayout>(R.id.button_skip)
        skipButton.setOnClickListener {
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }
    }
}