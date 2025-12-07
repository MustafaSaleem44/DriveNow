package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.repository.AuthRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class ProfilePage : AppCompatActivity() {
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var accountTypeText: TextView
    private lateinit var userNameText: TextView
    private lateinit var logoutButton: View
    private lateinit var companyDashboardButton: View
    
    private lateinit var authRepository: AuthRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val database = AppDatabase.getDatabase(this)
        authRepository = AuthRepository(database.userDao())
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadUserProfile()
    }

    private fun initializeViews() {
        fullNameEditText = findViewById(R.id.edit_full_name)
        emailEditText = findViewById(R.id.edit_email)
        phoneEditText = findViewById(R.id.edit_phone)
        accountTypeText = findViewById(R.id.text_account_type)
        userNameText = findViewById(R.id.text_user_name)
        logoutButton = findViewById(R.id.button_logout)
        companyDashboardButton = findViewById(R.id.button_company_dashboard)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.button_edit).setOnClickListener {
            toggleEditMode()
        }
    }

    private fun setupClickListeners() {
        logoutButton.setOnClickListener {
            showLogoutConfirmation()
        }

        val userType = prefsHelper.getUserType()
        if (userType == "COMPANY") {
            companyDashboardButton.visibility = View.VISIBLE
            companyDashboardButton.setOnClickListener {
                startActivity(Intent(this, CompanyDashboardOverview::class.java))
                finish()
            }
        } else {
            companyDashboardButton.visibility = View.GONE
        }
    }

    private fun loadUserProfile() {
        val userId = prefsHelper.getUserId()
        if (userId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            authRepository.getCurrentUser(userId)?.let { user ->
                userNameText.text = user.fullName
                fullNameEditText.setText(user.fullName)
                emailEditText.setText(user.email)
                phoneEditText.setText(user.phoneNumber ?: "")
                
                accountTypeText.text = if (user.userType == com.example.drivenow.data.UserType.COMPANY) {
                    "Company Account"
                } else {
                    "Customer Account"
                }

                // Make fields read-only initially
                fullNameEditText.isEnabled = false
                emailEditText.isEnabled = false
                phoneEditText.isEnabled = false
            }
        }
    }

    private fun toggleEditMode() {
        val isEnabled = fullNameEditText.isEnabled
        
        fullNameEditText.isEnabled = !isEnabled
        phoneEditText.isEnabled = !isEnabled
        // Email should not be editable

        if (!isEnabled) {
            // Entering edit mode
            findViewById<View>(R.id.button_edit).alpha = 0.5f
        } else {
            // Save changes
            saveProfile()
            findViewById<View>(R.id.button_edit).alpha = 1.0f
        }
    }

    private fun saveProfile() {
        val userId = prefsHelper.getUserId() ?: return
        
        lifecycleScope.launch {
            val user = authRepository.getCurrentUser(userId)
            user?.let {
                // Update user locally
                val updatedUser = it.copy(
                    fullName = fullNameEditText.text.toString().trim(),
                    phoneNumber = phoneEditText.text.toString().trim()
                )
                // Save to database
                val database = AppDatabase.getDatabase(this@ProfilePage)
                database.userDao().updateUser(updatedUser)
                userNameText.text = updatedUser.fullName
            }
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        prefsHelper.clearSession()
        startActivity(Intent(this, UserLogin::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}