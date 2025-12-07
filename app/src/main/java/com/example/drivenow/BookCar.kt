package com.example.drivenow

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Booking
import com.example.drivenow.data.BookingStatus
import com.example.drivenow.repository.BookingRepository
import com.example.drivenow.repository.CarRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BookCar : AppCompatActivity() {
    private lateinit var carNameText: TextView
    private lateinit var carDetailsText: TextView
    private lateinit var priceText: TextView
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var pickupLocationEditText: EditText
    private lateinit var pickupTimeEditText: EditText
    private lateinit var totalAmountText: TextView
    private lateinit var taxText: TextView
    private lateinit var finalTotalText: TextView
    private lateinit var confirmButton: Button
    
    private lateinit var carRepository: CarRepository
    private lateinit var bookingRepository: BookingRepository
    private lateinit var prefsHelper: SharedPreferencesHelper
    
    private var carId: String? = null
    private var dailyRate: Double = 0.0
    private var companyId: String? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_car)

        carId = intent.getStringExtra("carId")
        if (carId == null) {
            finish()
            return
        }

        val database = AppDatabase.getDatabase(this)
        carRepository = CarRepository(database.carDao(), this)
        bookingRepository = BookingRepository(database.bookingDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadCarInfo()
    }

    private fun initializeViews() {
        carNameText = findViewById(R.id.text_car_name)
        carDetailsText = findViewById(R.id.text_car_details)
        priceText = findViewById(R.id.text_price_per_day)
        startDateEditText = findViewById(R.id.edit_start_date)
        endDateEditText = findViewById(R.id.edit_end_date)
        pickupLocationEditText = findViewById(R.id.edit_pickup_location)
        pickupTimeEditText = findViewById(R.id.edit_pickup_time)
        totalAmountText = findViewById(R.id.text_total_amount)
        taxText = findViewById(R.id.text_tax)
        finalTotalText = findViewById(R.id.text_final_total)
        confirmButton = findViewById(R.id.button_confirm_booking)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        startDateEditText.setOnClickListener {
            showDatePicker(true)
        }

        endDateEditText.setOnClickListener {
            showDatePicker(false)
        }

        // Update total when dates change
        startDateEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) calculateTotal()
        }
        endDateEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) calculateTotal()
        }

        confirmButton.setOnClickListener {
            createBooking()
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }

            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val dateString = dateFormat.format(selectedCalendar.time)

            if (isStartDate) {
                startDateEditText.setText(dateString)
                startDateEditText.tag = selectedCalendar.timeInMillis
                // Auto-set end date to next day
                if (endDateEditText.text.isEmpty()) {
                    selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    endDateEditText.setText(dateFormat.format(selectedCalendar.time))
                    endDateEditText.tag = selectedCalendar.timeInMillis
                }
            } else {
                endDateEditText.setText(dateString)
                endDateEditText.tag = selectedCalendar.timeInMillis
            }

            calculateTotal()
        }, year, month, day).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
            show()
        }
    }

    private fun calculateTotal() {
        val startDate = startDateEditText.tag as? Long
        val endDate = endDateEditText.tag as? Long

        if (startDate != null && endDate != null && dailyRate > 0) {
            val days = ((endDate - startDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
            val subtotal = dailyRate * days
            val tax = subtotal * 0.10
            val total = subtotal + tax

            totalAmountText.text = "$${String.format("%.2f", subtotal)}"
            taxText.text = "$${String.format("%.2f", tax)}"
            finalTotalText.text = "$${String.format("%.2f", total)}"
            
            confirmButton.text = "Confirm Booking - $${String.format("%.2f", total)}"
        }
    }

    private fun loadCarInfo() {
        carId?.let { id ->
            lifecycleScope.launch {
                carRepository.getCarById(id)?.let { car ->
                    dailyRate = car.dailyRate
                    companyId = car.companyId
                    
                    carNameText.text = car.carName
                    carDetailsText.text = "${car.modelYear} • ${car.carType}"
                    priceText.text = "$${car.dailyRate.toInt()}/day"
                    
                    pickupLocationEditText.setText(car.pickupLocation ?: "")
                }
            }
        }
    }

    private fun createBooking() {
        val userId = prefsHelper.getUserId()
        if (userId == null || carId == null || companyId == null) {
            return
        }

        val startDate = startDateEditText.tag as? Long
        val endDate = endDateEditText.tag as? Long

        if (startDate == null || endDate == null) {
            startDateEditText.error = "Please select dates"
            return
        }

        if (endDate <= startDate) {
            endDateEditText.error = "End date must be after start date"
            return
        }

        val pickupLocation = pickupLocationEditText.text.toString().trim()
        if (pickupLocation.isEmpty()) {
            pickupLocationEditText.error = "Pickup location is required"
            return
        }

        lifecycleScope.launch {
            val car = carRepository.getCarById(carId!!)
            if (car == null) {
                return@launch
            }

            val days = ((endDate - startDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
            val subtotal = dailyRate * days
            val tax = subtotal * 0.10
            val total = subtotal + tax

            val booking = Booking(
                id = "booking_${System.currentTimeMillis()}",
                userId = userId,
                companyId = companyId!!,
                carId = carId!!,
                carName = car.carName,
                startDate = startDate,
                endDate = endDate,
                pickupLocation = pickupLocation,
                pickupTime = pickupTimeEditText.text.toString().trim(),
                totalAmount = total,
                tax = tax,
                status = BookingStatus.PENDING,
                paymentMethod = "Credit Card"
            )

            bookingRepository.createBooking(booking).onSuccess {
                startActivity(Intent(this@BookCar, Bookings::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }.onFailure { exception ->
                // Show error
                android.widget.Toast.makeText(
                    this@BookCar,
                    exception.message ?: "Booking failed",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}