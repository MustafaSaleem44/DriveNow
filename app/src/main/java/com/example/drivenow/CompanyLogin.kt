package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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

class CompanyLogin : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordVisibilityButton: ImageView
    private lateinit var loginButton: TextView
    private lateinit var signUpButton: TextView
    private var isPasswordVisible = false
    
    private lateinit var authRepository: AuthRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_login)

        // Initialize database and repository
        val database = AppDatabase.getDatabase(this)
        authRepository = AuthRepository(database.userDao())
        prefsHelper = SharedPreferencesHelper(this)

        // Check if already logged in
        if (prefsHelper.isLoggedIn() && prefsHelper.getUserType() == "COMPANY") {
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
    }

    private fun setupClickListeners() {
        passwordVisibilityButton.setOnClickListener {
            togglePasswordVisibility()
        }

        loginButton.setOnClickListener {
            performLogin()
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java).apply {
                putExtra("userType", "COMPANY")
            }
            startActivity(intent)
        }
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
                val result = authRepository.login(email, password, UserType.COMPANY)

                result.onSuccess { user ->
                    // Verify it's a company account
                    if (user.userType != UserType.COMPANY) {
                        loginButton.isEnabled = true
                        loginButton.text = "Login"
                        Toast.makeText(
                            this@CompanyLogin,
                            "This is not a company account",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }

                    // Save session
                    prefsHelper.saveUserSession(
                        user.id,
                        user.userType.name,
                        user.email
                    )

                    // Sync data if online
                    if (NetworkUtils.isNetworkAvailable(this@CompanyLogin)) {
                        com.example.drivenow.service.DataSyncService(this@CompanyLogin).fetchAndSyncFromServer(user.id)
                    }

                    // Navigate to company dashboard
                    navigateToMainScreen()
                }.onFailure { exception ->
                    loginButton.isEnabled = true
                    loginButton.text = "Login"
                    Toast.makeText(
                        this@CompanyLogin,
                        exception.message ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                loginButton.isEnabled = true
                loginButton.text = "Login"
                Toast.makeText(
                    this@CompanyLogin,
                    "Network error. Please check your connection.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, CompanyDashboardOverview::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}