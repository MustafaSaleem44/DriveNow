package com.example.drivenow // CHANGE THIS to your actual package name

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SignupActivity : AppCompatActivity() {

    // Declare Views
    private lateinit var btnModeCustomer: LinearLayout
    private lateinit var btnModeCompany: LinearLayout
    private lateinit var textCustomer: TextView
    private lateinit var textCompany: TextView
    private lateinit var iconCustomer: ImageView
    private lateinit var iconCompany: ImageView

    private lateinit var labelName: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvLoginLink: TextView

    private var isCompanyMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 1. Initialize Views
        btnModeCustomer = findViewById(R.id.btn_mode_customer)
        btnModeCompany = findViewById(R.id.btn_mode_company)
        textCustomer = findViewById(R.id.text_mode_customer)
        textCompany = findViewById(R.id.text_mode_company)
        iconCustomer = findViewById(R.id.icon_mode_customer)
        iconCompany = findViewById(R.id.icon_mode_company)

        labelName = findViewById(R.id.label_name)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnSignup = findViewById(R.id.btn_signup)
        tvLoginLink = findViewById(R.id.tv_login_link)

        // 2. Handle Toggles
        btnModeCustomer.setOnClickListener { updateToggleState(false) }
        btnModeCompany.setOnClickListener { updateToggleState(true) }

        // 3. Handle Sign Up Button
        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DatabaseHelper(this)
            if (isCompanyMode) {
                val company = Company(name = name, email = email, password = pass)
                val result = dbHelper.addCompany(company)
                if (result > -1) {
                    Toast.makeText(this, "Company Registered Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                val customer = Customer(name = name, email = email, password = pass)
                val result = dbHelper.addCustomer(customer)
                if (result > -1) {
                    Toast.makeText(this, "Customer Registered Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 4. Handle "Already have an account?" link
        tvLoginLink.setOnClickListener {
            finish() // Simply closes this activity and returns to LoginActivity
        }
    }

    private fun updateToggleState(isCompany: Boolean) {
        isCompanyMode = isCompany

        val activeColor = ContextCompat.getColor(this, R.color.white)
        val inactiveColor = ContextCompat.getColor(this, R.color.text_secondary)

        if (isCompany) {
            // Company Active
            btnModeCompany.setBackgroundResource(R.drawable.bg_toggle_active)
            textCompany.setTextColor(activeColor)
            iconCompany.setColorFilter(activeColor)

            // Customer Inactive
            btnModeCustomer.background = null
            textCustomer.setTextColor(inactiveColor)
            iconCustomer.setColorFilter(inactiveColor)

            // Change Label
            labelName.text = "Company Name"
            etName.hint = "DriveNow Rentals"
        } else {
            // Customer Active
            btnModeCustomer.setBackgroundResource(R.drawable.bg_toggle_active)
            textCustomer.setTextColor(activeColor)
            iconCustomer.setColorFilter(activeColor)

            // Company Inactive
            btnModeCompany.background = null
            textCompany.setTextColor(inactiveColor)
            iconCompany.setColorFilter(inactiveColor)

            // Change Label
            labelName.text = "Full Name"
            etName.hint = "John Doe"
        }
    }
}