package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.UserType
import com.example.drivenow.repository.AuthRepository
import com.example.drivenow.utils.NetworkUtils
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class UserLogin : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordVisibilityButton: ImageView
    private lateinit var loginButton: TextView
    private lateinit var signUpButton: TextView
    private lateinit var customerButton: View
    private lateinit var companyButton: View
    private var isCustomerSelected = true
    private var isPasswordVisible = false
    
    private lateinit var authRepository: AuthRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        // Initialize database and repository
        val database = AppDatabase.getDatabase(this)
        authRepository = AuthRepository(database.userDao())
        prefsHelper = SharedPreferencesHelper(this)

        // Check if already logged in
        if (prefsHelper.isLoggedIn()) {
            navigateToMainScreen()
            return
        }

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        emailEditText = findViewById(R.id.edit_email)
        passwordEditText = findViewById(R.id.edit_password)
        passwordVisibilityButton = findViewById(R.id.button_password_visibility)
        loginButton = findViewById(R.id.text_login)
        signUpButton = findViewById(R.id.text_sign_up)
        customerButton = findViewById(R.id.button_customer)
        companyButton = findViewById(R.id.button_company)
        
        // Set default selection
        updateUserTypeSelection(true)
    }

    private fun setupClickListeners() {
        customerButton.setOnClickListener {
            isCustomerSelected = true
            updateUserTypeSelection(true)
        }

        companyButton.setOnClickListener {
            isCustomerSelected = false
            updateUserTypeSelection(false)
        }

        passwordVisibilityButton.setOnClickListener {
            togglePasswordVisibility()
        }

        loginButton.setOnClickListener {
            performLogin()
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java).apply {
                putExtra("userType", if (isCustomerSelected) "CUSTOMER" else "COMPANY")
            }
            startActivity(intent)
        }
    }

    private fun updateUserTypeSelection(isCustomer: Boolean) {
        val customerBg = if (isCustomer) R.drawable.button_customer else R.drawable.button_company
        val companyBg = if (!isCustomer) R.drawable.button_company else R.drawable.button_customer
        
        customerButton.setBackgroundResource(customerBg)
        companyButton.setBackgroundResource(companyBg)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        passwordEditText.transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun performLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            emailEditText.requestFocus()
            return
        }

        // Show loading
        loginButton.isEnabled = false
        loginButton.text = "Logging in..."

        lifecycleScope.launch {
            try {
                val userType = if (isCustomerSelected) UserType.CUSTOMER else UserType.COMPANY
                val result = authRepository.login(email, password, userType)

                result.onSuccess { user ->
                    // Save session
                    prefsHelper.saveUserSession(
                        user.id,
                        user.userType.name,
                        user.email
                    )

                    // Sync data if online
                    if (NetworkUtils.isNetworkAvailable(this@UserLogin)) {
                        com.example.drivenow.service.DataSyncService(this@UserLogin).fetchAndSyncFromServer(user.id)
                    }

                    // Navigate to appropriate screen
                    navigateToMainScreen()
                }.onFailure { exception ->
                    loginButton.isEnabled = true
                    loginButton.text = "Login"
                    Toast.makeText(
                        this@UserLogin,
                        exception.message ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                loginButton.isEnabled = true
                loginButton.text = "Login"
                Toast.makeText(
                    this@UserLogin,
                    "Network error. Please check your connection.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun navigateToMainScreen() {
        val userType = prefsHelper.getUserType()
        val intent = if (userType == "COMPANY") {
            Intent(this, CompanyDashboardOverview::class.java)
        } else {
            Intent(this, MainPage::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}