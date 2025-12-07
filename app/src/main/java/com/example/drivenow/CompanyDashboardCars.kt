package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Car
import com.example.drivenow.repository.CarRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class CompanyDashboardCars : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var carRepository: CarRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_cars)

        val database = AppDatabase.getDatabase(this)
        carRepository = CarRepository(database.carDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadCars()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_cars)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        findViewById<TextView>(R.id.text_overview).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardOverview::class.java))
        }

        findViewById<TextView>(R.id.text_bookings).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardBookings::class.java))
        }

        findViewById<View>(R.id.button_add_car).setOnClickListener {
            startActivity(Intent(this, AddCar::class.java))
        }

        findViewById<TextView>(R.id.text_add_car).setOnClickListener {
            startActivity(Intent(this, AddCar::class.java))
        }

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun loadCars() {
        val companyId = prefsHelper.getUserId()
        if (companyId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            carRepository.getCarsByCompany(companyId).collect { cars ->
                recyclerView.adapter = CompanyCarAdapter(cars) { car, action ->
                    when (action) {
                        "edit" -> {
                            // TODO: Implement edit car
                        }
                        "delete" -> {
                            showDeleteConfirmation(car)
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteConfirmation(car: Car) {
        AlertDialog.Builder(this)
            .setTitle("Delete Car")
            .setMessage("Are you sure you want to delete ${car.carName}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    carRepository.deleteCar(car.id).onSuccess {
                        // Car deleted
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private class CompanyCarAdapter(
        private val cars: List<Car>,
        private val onAction: (Car, String) -> Unit
    ) : RecyclerView.Adapter<CompanyCarAdapter.CarViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_company_car, parent, false)
            return CarViewHolder(view)
        }

        override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
            holder.bind(cars[position])
        }

        override fun getItemCount() = cars.size

        inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val carImage: ImageView = itemView.findViewById(R.id.image_car)
            private val carName: TextView = itemView.findViewById(R.id.text_car_name)
            private val carDetails: TextView = itemView.findViewById(R.id.text_car_details)
            private val priceText: TextView = itemView.findViewById(R.id.text_price)
            private val bookingsText: TextView = itemView.findViewById(R.id.text_bookings)
            private val statusBadge: TextView = itemView.findViewById(R.id.text_status)
            private val editButton: View = itemView.findViewById(R.id.button_edit)
            private val deleteButton: View = itemView.findViewById(R.id.button_delete)

            fun bind(car: Car) {
                carName.text = car.carName
                carDetails.text = "${car.modelYear} • ${car.carType}"
                priceText.text = "$${car.dailyRate.toInt()}/day"
                bookingsText.text = "${car.bookingCount} bookings"

                // Status badge
                statusBadge.text = car.status.name
                val statusColor = when (car.status) {
                    com.example.drivenow.data.CarStatus.AVAILABLE -> 0xFF03DAC5.toInt()
                    com.example.drivenow.data.CarStatus.RENTED -> 0xFFFF9500.toInt()
                    com.example.drivenow.data.CarStatus.MAINTENANCE -> 0xFFFF3B30.toInt()
                }
                statusBadge.setBackgroundColor(statusColor)

                // Load image
                car.imageUrl?.let { imageUrl ->
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.image_fallback)
                        .error(R.drawable.image_fallback)
                        .into(carImage)
                } ?: carImage.setImageResource(R.drawable.image_fallback)

                editButton.setOnClickListener {
                    onAction(car, "edit")
                }

                deleteButton.setOnClickListener {
                    onAction(car, "delete")
                }
            }
        }
    }
}