package com.example.drivenow

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookCar : AppCompatActivity() {

    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var tvCalcDetail: TextView
    private lateinit var tvCalcTotal: TextView
    private lateinit var tvTaxTotal: TextView
    private lateinit var tvFinalTotal: TextView
    private lateinit var btnConfirm: Button
    
    // Header Info
    private lateinit var tvCarName: TextView
    private lateinit var tvCarRateDetail: TextView

    private var startTimestamp: Long = 0
    private var endTimestamp: Long = 0
    private var carPricePerDay = 45.0
    
    // Data from Intent
    private var carName: String = ""
    private var carType: String = ""
    private var carImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_car)

        // Initialize Views
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        tvStartDate = findViewById(R.id.tv_start_date_display)
        tvEndDate = findViewById(R.id.tv_end_date_display)
        tvCalcDetail = findViewById(R.id.tv_calc_detail)
        tvCalcTotal = findViewById(R.id.tv_calc_total)
        tvTaxTotal = findViewById(R.id.tv_tax_total)
        tvFinalTotal = findViewById(R.id.tv_final_total)
        btnConfirm = findViewById(R.id.btn_confirm_booking)
        
        // Car Header Views
        val tvName = findViewById<TextView>(R.id.tv_car_name)
        val tvType = findViewById<TextView>(R.id.tv_car_type)
        val tvPriceRate = findViewById<TextView>(R.id.tv_price_rate)
        val imgThumb = findViewById<ImageView>(R.id.img_car_thumb)

        val containerStart = findViewById<LinearLayout>(R.id.container_start_date)
        val containerEnd = findViewById<LinearLayout>(R.id.container_end_date)

        // Get Data from Intent
        carName = intent.getStringExtra("CAR_NAME") ?: "Car"
        val priceString = intent.getStringExtra("CAR_PRICE") ?: "$45" // e.g. "$45/day" or "$45"
        carType = intent.getStringExtra("CAR_TYPE") ?: "Sedan"
        carImage = intent.getStringExtra("CAR_IMAGE") ?: ""

        // Set Header Data
        tvName.text = carName
        tvType.text = carType
        tvPriceRate.text = priceString
        
        // Load Image
        if (carImage.isNotEmpty()) {
            try {
                val imageBytes = android.util.Base64.decode(carImage, android.util.Base64.DEFAULT)
                val decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgThumb.setImageBitmap(decodedImage)
                imgThumb.clearColorFilter() // Remove tint
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Parse Price
        carPricePerDay = try {
            // Remove '$', '/day', and clean spaces
            val cleanPrice = priceString.replace("$", "").replace("/day", "").trim()
            cleanPrice.toDouble()
        } catch (e: Exception) {
            45.0 // Fallback
        }

        // Back Button
        btnBack.setOnClickListener { finish() }

        // Date Pickers
        containerStart.setOnClickListener { showDatePicker(isStart = true) }
        containerEnd.setOnClickListener { showDatePicker(isStart = false) }

        // Confirm Button
        btnConfirm.setOnClickListener {
            if (startTimestamp == 0L || endTimestamp == 0L) {
                Toast.makeText(this, "Please select dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Get User Email
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userEmail = sharedPref.getString("USER_EMAIL", "guest@example.com") ?: "guest@example.com"

            // Create Booking
            val diffMs = endTimestamp - startTimestamp
            val days = TimeUnit.MILLISECONDS.toDays(diffMs).toInt() + 1
            val subtotal = days * carPricePerDay
            val tax = subtotal * 0.10
            val total = subtotal + tax
            val priceFormatted = "$" + String.format("%.2f", total)
            
            val startDateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(startTimestamp))
            val endDateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(endTimestamp))
            val todayStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

            val booking = CustomerBooking(
                id = 0,
                carName = carName,
                carDetails = carType,
                startDate = startDateStr,
                endDate = endDateStr,
                location = "Downtown Center", // Hardcoded for now
                bookedOn = todayStr,
                totalAmount = priceFormatted,
                status = "Confirmed",
                customerEmail = userEmail
            )
            
            // 1. Save Locally
            val dbHelper = DatabaseHelper(this)
            dbHelper.addBooking(booking)

            // Notifications Locally
            val currentDate = SimpleDateFormat("MMM dd, HH:mm", Locale.US).format(Date())
            val paymentMsgLocal = "Payment of $priceFormatted has been processed successfully."
            dbHelper.addNotification(Notification(title = "Payment Successful", message = paymentMsgLocal, date = currentDate, userEmail = userEmail, type = "payment"))
            val bookingMsgLocal = "Your booking for $carName from $startDateStr is confirmed."
            dbHelper.addNotification(Notification(title = "Booking Confirmed", message = bookingMsgLocal, date = currentDate, userEmail = userEmail, type = "booking"))

            // 2. API Call
            RetrofitClient.instance.bookCar(
                booking.carName, booking.carDetails, booking.startDate, booking.endDate,
                booking.location, booking.bookedOn, booking.totalAmount, booking.status, booking.customerEmail
            ).enqueue(object : retrofit2.Callback<ApiResponse> {
                override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        sendNotifications(userEmail, priceFormatted, carName, startDateStr)
                    } else {
                        // Even if API fails, we saved locally
                        Toast.makeText(this@BookCar, "Booking Saved Locally. Sync Failed.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BookCar, CustomerHomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                     // Network error, but saved locally
                     Toast.makeText(this@BookCar, "Booking Saved Locally.", Toast.LENGTH_SHORT).show()
                     navigateHome()
                }
            })
        }
    }

    private fun sendNotifications(userEmail: String, priceFormatted: String, carName: String, startDateStr: String) {
        val currentDate = SimpleDateFormat("MMM dd, HH:mm", Locale.US).format(Date())

        // 1. Payment Successful
        val paymentMsg = "Payment of $priceFormatted has been processed successfully."
        RetrofitClient.instance.addNotification("Payment Successful", paymentMsg, currentDate, userEmail, "payment")
            .enqueue(object : retrofit2.Callback<ApiResponse> {
                override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                    // 2. Booking Confirmed (Chained)
                    val bookingMsg = "Your booking for $carName from $startDateStr is confirmed."
                    RetrofitClient.instance.addNotification("Booking Confirmed", bookingMsg, currentDate, userEmail, "booking")
                        .enqueue(object : retrofit2.Callback<ApiResponse> {
                            override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                                navigateHome()
                            }
                            override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                                navigateHome() // Navigate anyway
                            }
                        })
                }

                override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                    navigateHome() // Navigate anyway
                }
            })
    }

    private fun navigateHome() {
        Toast.makeText(this@BookCar, "Booking Confirmed!", Toast.LENGTH_LONG).show()
        val intent = Intent(this@BookCar, CustomerHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showDatePicker(isStart: Boolean) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)
                val dateString = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedCalendar.time)

                if (isStart) {
                    startTimestamp = selectedCalendar.timeInMillis
                    tvStartDate.text = dateString
                } else {
                    endTimestamp = selectedCalendar.timeInMillis
                    tvEndDate.text = dateString
                }
                calculatePrice()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Prevent selecting past dates
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun calculatePrice() {
        if (startTimestamp != 0L && endTimestamp != 0L) {
            if (endTimestamp < startTimestamp) {
                Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                tvEndDate.text = "Select End Date"
                endTimestamp = 0
                return
            }

            val diffMs = endTimestamp - startTimestamp
            // Calculate days (add 1 to include the start day itself as a rental day)
            val days = TimeUnit.MILLISECONDS.toDays(diffMs).toInt() + 1

            val subtotal = days * carPricePerDay
            val tax = subtotal * 0.10 // 10% tax
            val total = subtotal + tax

            // Update UI
            tvCalcDetail.text = "$$carPricePerDay/day x $days days"
            tvCalcTotal.text = "$${String.format("%.2f", subtotal)}"
            tvTaxTotal.text = "$${String.format("%.2f", tax)}"
            tvFinalTotal.text = "$${String.format("%.2f", total)}"

            btnConfirm.text = "Confirm Booking - $${String.format("%.2f", total)}"
        }
    }
}