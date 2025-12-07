package com.example.drivenow

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class AddCarActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private var selectedImageBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        // 1. Initialize Views
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnSubmit = findViewById<Button>(R.id.btn_add_car_submit)
        val btnSelectImage = findViewById<Button>(R.id.btn_select_image)
        imgPreview = findViewById(R.id.img_car_preview)

        val spinnerType = findViewById<Spinner>(R.id.spinner_car_type)
        val spinnerSeats = findViewById<Spinner>(R.id.spinner_seats)
        val spinnerFuel = findViewById<Spinner>(R.id.spinner_fuel)

        val etName = findViewById<EditText>(R.id.et_car_name)
        val etYear = findViewById<EditText>(R.id.et_model_year)
        val etRate = findViewById<EditText>(R.id.et_daily_rate)

        // 2. Setup Spinners with Dummy Data
        setupSpinner(spinnerType, listOf("Select Type", "Sedan", "SUV", "Convertible", "Hatchback", "Electric"))
        setupSpinner(spinnerSeats, listOf("Select Seats", "2 Seats", "4 Seats", "5 Seats", "7 Seats"))
        setupSpinner(spinnerFuel, listOf("Select Fuel", "Gasoline", "Diesel", "Hybrid", "Electric"))

        // 3. Button Listeners
        btnBack.setOnClickListener { finish() }
        btnCancel.setOnClickListener { finish() }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        btnSubmit.setOnClickListener {
            if (validateInputs(etName, etYear, etRate, spinnerType)) {
                val dbHelper = DatabaseHelper(this)
                // Use the selected items from spinners
                val year = etYear.text.toString()
                val carType = spinnerType.selectedItem.toString()
                // Format: "2024 • Sedan"
                val fullType = "$year • $carType"
                
                val car = Car(
                    id = 0,
                    name = etName.text.toString(),
                    type = fullType,
                    pricePerDay = "$" + etRate.text.toString(),
                    status = "Available",
                    image = selectedImageBase64,
                    seats = spinnerSeats.selectedItem.toString(),
                    fuelType = spinnerFuel.selectedItem.toString()
                )
                
                val result = dbHelper.addCar(car)
                if (result > -1) {
                    Toast.makeText(this, "Car Added Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to Add Car", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imgPreview.setImageBitmap(bitmap)
                imgPreview.visibility = View.VISIBLE
                
                val outputStream = ByteArrayOutputStream()
                // Compress to 50% quality to save space
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                val byteArray = outputStream.toByteArray()
                selectedImageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.WHITE)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK)
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun validateInputs(name: EditText, year: EditText, rate: EditText, type: Spinner): Boolean {
        if (name.text.isEmpty()) {
            name.error = "Required"
            return false
        }
        if (year.text.isEmpty()) {
            year.error = "Required"
            return false
        }
        if (rate.text.isEmpty()) {
            rate.error = "Required"
            return false
        }
        if (type.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a car type", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}