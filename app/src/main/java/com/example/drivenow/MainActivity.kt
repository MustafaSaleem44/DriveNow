package com.example.drivenow // CHANGE THIS to your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Define data for the 3 pages
    private val titles = listOf("Book Instantly", "Find Cars Easily", "Drive Anywhere")
    private val descriptions = listOf(
        "Reserve your preferred car in just a few taps with our instant booking system.",
        "Browse through hundreds of available cars in your area with our smart search feature.",
        "Pick up your car from convenient locations and enjoy the freedom of the road."
    )
    private val icons = listOf(R.drawable.ic_location, R.drawable.ic_search, R.drawable.ic_car_type)

    private var currentPage = 0

    private lateinit var ivIcon: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var btnNext: TextView
    private lateinit var btnSkip: TextView
    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this matches the XML file name

        // Initialize Views
        ivIcon = findViewById(R.id.iv_onboarding_icon)
        tvTitle = findViewById(R.id.text_heading)
        tvDesc = findViewById(R.id.text_description)
        btnNext = findViewById(R.id.btn_next)
        btnSkip = findViewById(R.id.btn_skip)
        dot1 = findViewById(R.id.dot_1)
        dot2 = findViewById(R.id.dot_2)
        dot3 = findViewById(R.id.dot_3)

        // Setup Buttons
        btnNext.setOnClickListener {
            if (currentPage < 2) {
                currentPage++
                updateUI()
            } else {
                // Last page: Go to Login/Home
                navigateToLogin()
            }
        }

        btnSkip.setOnClickListener {
            navigateToLogin()
        }

        // Initial UI Load
        updateUI()
    }

    private fun updateUI() {
        // Update Text and Icon
        tvTitle.text = titles[currentPage]
        tvDesc.text = descriptions[currentPage]
        ivIcon.setImageResource(icons[currentPage])

        // Update Dots
        updateDots(currentPage)

        // Change "Next" to "Get Started" on the last page
        if (currentPage == 2) {
            btnNext.text = "Get Started >"
            btnNext.setTextColor(resources.getColor(R.color.white, theme))
            btnNext.setBackgroundResource(R.drawable.indicator_active) // Reuse active shape for button bg
            btnNext.setPadding(40, 20, 40, 20) // Add padding for the button look
        } else {
            btnNext.text = "Next >"
            btnNext.setTextColor(resources.getColor(R.color.purple_primary, theme))
            btnNext.background = null
        }
    }

    private fun updateDots(position: Int) {
        // Reset all to inactive
        dot1.setImageResource(R.drawable.indicator_inactive)
        dot2.setImageResource(R.drawable.indicator_inactive)
        dot3.setImageResource(R.drawable.indicator_inactive)

        // Set current to active
        when (position) {
            0 -> dot1.setImageResource(R.drawable.indicator_active)
            1 -> dot2.setImageResource(R.drawable.indicator_active)
            2 -> dot3.setImageResource(R.drawable.indicator_active)
        }
    }

    private fun navigateToLogin() {
        // Intent logic to open LoginActivity (Page 4)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}