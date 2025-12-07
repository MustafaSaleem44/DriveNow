package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenow.adapter.CarAdapter
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Car
import com.example.drivenow.data.CarStatus
import com.example.drivenow.repository.CarRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class MainPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var greetingText: TextView
    private lateinit var allFilter: View
    private lateinit var sedanFilter: View
    private lateinit var suvFilter: View
    private lateinit var convertibleFilter: View
    private lateinit var electricFilter: View
    private lateinit var notificationButton: ImageView
    private lateinit var profileButton: ImageView
    private lateinit var bookingsButton: ImageView
    
    private lateinit var carRepository: CarRepository
    private lateinit var prefsHelper: SharedPreferencesHelper
    private var currentFilter: String? = null
    private var allCars: List<Car> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val database = AppDatabase.getDatabase(this)
        carRepository = CarRepository(database.carDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadCars()
        setupSearch()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_cars)
        searchEditText = findViewById(R.id.edit_search)
        greetingText = findViewById(R.id.text_greeting)
        allFilter = findViewById(R.id.button_filter_all)
        sedanFilter = findViewById(R.id.button_filter_sedan)
        suvFilter = findViewById(R.id.button_filter_suv)
        convertibleFilter = findViewById(R.id.button_filter_convertible)
        electricFilter = findViewById(R.id.button_filter_electric)
        notificationButton = findViewById(R.id.image_icon6)
        profileButton = findViewById(R.id.image_icon5)
        bookingsButton = findViewById(R.id.image_icon1)

        // Set up greeting
        val userName = prefsHelper.getUserEmail()?.split("@")?.get(0) ?: "User"
        greetingText.text = "Hello, $userName!"

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        // Navigation
        notificationButton.setOnClickListener {
            startActivity(Intent(this, Notifications::class.java))
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        bookingsButton.setOnClickListener {
            startActivity(Intent(this, Bookings::class.java))
        }

        // Filters
        allFilter.setOnClickListener {
            currentFilter = null
            updateFilterSelection(null)
            filterCars()
        }

        sedanFilter.setOnClickListener {
            currentFilter = "Sedan"
            updateFilterSelection("Sedan")
            filterCars()
        }

        suvFilter.setOnClickListener {
            currentFilter = "Suv"
            updateFilterSelection("Suv")
            filterCars()
        }

        convertibleFilter.setOnClickListener {
            currentFilter = "Convertible"
            updateFilterSelection("Convertible")
            filterCars()
        }

        electricFilter.setOnClickListener {
            currentFilter = "Electric"
            updateFilterSelection("Electric")
            filterCars()
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterCars()
            }
        })
    }

    private fun loadCars() {
        lifecycleScope.launch {
            // Refresh from server first
            carRepository.refreshCarsFromServer()

            // Observe cars from database
            carRepository.getAllCars().collect { cars ->
                allCars = cars.filter { it.status == CarStatus.AVAILABLE }
                filterCars()
            }
        }
    }

    private fun filterCars() {
        val searchQuery = searchEditText.text.toString().trim()
        
        var filtered = allCars

        // Apply search filter
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.carName.contains(searchQuery, ignoreCase = true) ||
                it.description?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        // Apply type filter
        currentFilter?.let { filter ->
            filtered = filtered.filter {
                it.carType.equals(filter, ignoreCase = true)
            }
        }

        // Update RecyclerView
        recyclerView.adapter = CarAdapter(filtered) { car ->
            val intent = Intent(this, CarDetails::class.java).apply {
                putExtra("carId", car.id)
            }
            startActivity(intent)
        }
    }

    private fun updateFilterSelection(selectedFilter: String?) {
        val selectedBg = R.drawable.layer_button8 // Purple background
        val unselectedBg = R.drawable.layer_button9 // Gray background

        allFilter.setBackgroundResource(if (selectedFilter == null) selectedBg else unselectedBg)
        sedanFilter.setBackgroundResource(if (selectedFilter == "Sedan") selectedBg else unselectedBg)
        suvFilter.setBackgroundResource(if (selectedFilter == "Suv") selectedBg else unselectedBg)
        convertibleFilter.setBackgroundResource(if (selectedFilter == "Convertible") selectedBg else unselectedBg)
        electricFilter.setBackgroundResource(if (selectedFilter == "Electric") selectedBg else unselectedBg)
    }
}