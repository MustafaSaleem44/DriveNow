package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddCar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        val addButton = findViewById<TextView>(R.id.btn_text_add_fleet)
        addButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardCars::class.java)
            startActivity(intent)
        }
    }
}