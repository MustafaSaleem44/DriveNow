package com.example.drivenow // CHANGE THIS to your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {

    // Declare Views
    private lateinit var btnModeCustomer: LinearLayout
    private lateinit var btnModeCompany: LinearLayout
    private lateinit var textCustomer: TextView
    private lateinit var textCompany: TextView
    private lateinit var iconCustomer: ImageView
    private lateinit var iconCompany: ImageView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignup: TextView

    // Track current mode (Default is Customer)
    private var isCompanyMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Initialize Views
        btnModeCustomer = findViewById(R.id.btn_mode_customer)
        btnModeCompany = findViewById(R.id.btn_mode_company)
        textCustomer = findViewById(R.id.text_mode_customer)
        textCompany = findViewById(R.id.text_mode_company)
        iconCustomer = findViewById(R.id.icon_mode_customer)
        iconCompany = findViewById(R.id.icon_mode_company)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvSignup = findViewById(R.id.tv_signup)

        // 2. Handle Toggle Clicks
        btnModeCustomer.setOnClickListener { updateToggleState(isCompany = false) }
        btnModeCompany.setOnClickListener { updateToggleState(isCompany = true) }

        // 3. Handle Login Click
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val table = if (isCompanyMode) "company" else "customer"

                RetrofitClient.instance.loginUser(email, password, table)
                    .enqueue(object : retrofit2.Callback<ApiResponse> {
                        override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                            if (response.isSuccessful && response.body()?.status == "success") {
                                // Save User Email
                                val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("USER_EMAIL", email)
                                    apply()
                                }

                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                
                                if (isCompanyMode) {
                                    val intent = Intent(this@LoginActivity, CompanyDashboardActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this@LoginActivity, CustomerHomeActivity::class.java)
                                    startActivity(intent)
                                }
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, response.body()?.message ?: "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Handle Sign Up Click (NEW CODE)
        tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            // We don't call finish() here so the user can go back to login if they want
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
        } else {
            // Customer Active
            btnModeCustomer.setBackgroundResource(R.drawable.bg_toggle_active)
            textCustomer.setTextColor(activeColor)
            iconCustomer.setColorFilter(activeColor)

            // Company Inactive
            btnModeCompany.background = null
            textCompany.setTextColor(inactiveColor)
            iconCompany.setColorFilter(inactiveColor)
        }
    }
}