// File: com/example/drivenow/BookCarActivity.kt
package com.example.drivenow

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
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

    private var startTimestamp: Long = 0
    private var endTimestamp: Long = 0
    private val carPricePerDay = 45.0 // Hardcoded for demo

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

        val containerStart = findViewById<LinearLayout>(R.id.container_start_date)
        val containerEnd = findViewById<LinearLayout>(R.id.container_end_date)

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
            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_LONG).show()
            // Here you would navigate to Booking Success page or Home
        }
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