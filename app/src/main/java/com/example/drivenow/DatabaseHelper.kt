package com.example.drivenow

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Insert Customer
    fun addCustomer(customer: Customer): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_CUST_NAME, customer.name)
        contentValues.put(COL_CUST_EMAIL, customer.email)
        contentValues.put(COL_CUST_PASS, customer.password)

        val result = db.insert(TABLE_CUSTOMER, null, contentValues)
        db.close()
        return result
    }

    // Insert Company
    fun addCompany(company: Company): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_COMP_NAME, company.name)
        contentValues.put(COL_COMP_EMAIL, company.email)
        contentValues.put(COL_COMP_PASS, company.password)

        val result = db.insert(TABLE_COMPANY, null, contentValues)
        db.close()
        return result
    }

    // Check Customer Login
    fun checkCustomer(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CUSTOMER WHERE $COL_CUST_EMAIL=? AND $COL_CUST_PASS=?", arrayOf(email, pass))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Check Company Login
    fun checkCompany(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COMPANY WHERE $COL_COMP_EMAIL=? AND $COL_COMP_PASS=?", arrayOf(email, pass))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Car Table
    companion object {
        private const val DATABASE_NAME = "DriveNow.db"
        private const val DATABASE_VERSION = 5 // Incremented version to 5

        // Customer Table
        const val TABLE_CUSTOMER = "Customer"
        const val COL_CUST_ID = "id"
        const val COL_CUST_NAME = "name"
        const val COL_CUST_EMAIL = "email"
        const val COL_CUST_PASS = "password"

        // Company Table
        const val TABLE_COMPANY = "Company"
        const val COL_COMP_ID = "id"
        const val COL_COMP_NAME = "name"
        const val COL_COMP_EMAIL = "email"
        const val COL_COMP_PASS = "password"

        // Car Table
        const val TABLE_CAR = "Car"
        const val COL_CAR_ID = "id"
        const val COL_CAR_NAME = "name"
        const val COL_CAR_TYPE = "type"
        const val COL_CAR_PRICE = "price"
        const val COL_CAR_STATUS = "status"
        const val COL_CAR_IMAGE = "image"
        const val COL_CAR_SEATS = "seats" 
        const val COL_CAR_FUEL = "fuel"   

        // Booking Table
        const val TABLE_BOOKING = "Booking"
        const val COL_BOOK_ID = "id"
        const val COL_BOOK_CAR_NAME = "car_name"
        const val COL_BOOK_CAR_DETAILS = "car_details"
        const val COL_BOOK_START = "start_date"
        const val COL_BOOK_END = "end_date"
        const val COL_BOOK_LOC = "location"
        const val COL_BOOK_DATE = "booked_on"
        const val COL_BOOK_TOTAL = "total_amount"
        const val COL_BOOK_STATUS = "status"
        const val COL_BOOK_CUST_EMAIL = "customer_email"

        // Notification Table
        const val TABLE_NOTIF = "Notification"
        const val COL_NOTIF_ID = "id"
        const val COL_NOTIF_TITLE = "title"
        const val COL_NOTIF_MSG = "message"
        const val COL_NOTIF_DATE = "date"
        const val COL_NOTIF_USER = "user_email"
        const val COL_NOTIF_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCustomerTable = ("CREATE TABLE " + TABLE_CUSTOMER + "("
                + COL_CUST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_CUST_NAME + " TEXT,"
                + COL_CUST_EMAIL + " TEXT,"
                + COL_CUST_PASS + " TEXT" + ")")
        db.execSQL(createCustomerTable)

        val createCompanyTable = ("CREATE TABLE " + TABLE_COMPANY + "("
                + COL_COMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_COMP_NAME + " TEXT,"
                + COL_COMP_EMAIL + " TEXT,"
                + COL_COMP_PASS + " TEXT" + ")")
        db.execSQL(createCompanyTable)

        val createCarTable = ("CREATE TABLE " + TABLE_CAR + "("
                + COL_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_CAR_NAME + " TEXT,"
                + COL_CAR_TYPE + " TEXT,"
                + COL_CAR_PRICE + " TEXT,"
                + COL_CAR_STATUS + " TEXT,"
                + COL_CAR_IMAGE + " TEXT,"
                + COL_CAR_SEATS + " TEXT,"
                + COL_CAR_FUEL + " TEXT" + ")")
        db.execSQL(createCarTable)

        val createBookingTable = ("CREATE TABLE " + TABLE_BOOKING + "("
                + COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_BOOK_CAR_NAME + " TEXT,"
                + COL_BOOK_CAR_DETAILS + " TEXT,"
                + COL_BOOK_START + " TEXT,"
                + COL_BOOK_END + " TEXT,"
                + COL_BOOK_LOC + " TEXT,"
                + COL_BOOK_DATE + " TEXT,"
                + COL_BOOK_TOTAL + " TEXT,"
                + COL_BOOK_STATUS + " TEXT,"
                + COL_BOOK_CUST_EMAIL + " TEXT" + ")")
        db.execSQL(createBookingTable)

        val createNotifTable = ("CREATE TABLE " + TABLE_NOTIF + "("
                + COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOTIF_TITLE + " TEXT,"
                + COL_NOTIF_MSG + " TEXT,"
                + COL_NOTIF_DATE + " TEXT,"
                + COL_NOTIF_USER + " TEXT,"
                + COL_NOTIF_TYPE + " TEXT" + ")")
        db.execSQL(createNotifTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Simple upgrade: drop and recreate. In production, you'd migrate data.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMPANY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CAR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKING")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIF")
        onCreate(db)
    }

    // Insert Booking
    fun addBooking(booking: CustomerBooking): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_BOOK_CAR_NAME, booking.carName)
        contentValues.put(COL_BOOK_CAR_DETAILS, booking.carDetails)
        contentValues.put(COL_BOOK_START, booking.startDate)
        contentValues.put(COL_BOOK_END, booking.endDate)
        contentValues.put(COL_BOOK_LOC, booking.location)
        contentValues.put(COL_BOOK_DATE, booking.bookedOn)
        contentValues.put(COL_BOOK_TOTAL, booking.totalAmount)
        contentValues.put(COL_BOOK_STATUS, booking.status)
        contentValues.put(COL_BOOK_CUST_EMAIL, booking.customerEmail)

        val result = db.insert(TABLE_BOOKING, null, contentValues)
        db.close()
        return result
    }

    // Insert Notification
    fun addNotification(notification: Notification): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NOTIF_TITLE, notification.title)
        contentValues.put(COL_NOTIF_MSG, notification.message)
        contentValues.put(COL_NOTIF_DATE, notification.date)
        contentValues.put(COL_NOTIF_USER, notification.userEmail)
        contentValues.put(COL_NOTIF_TYPE, notification.type)

        val result = db.insert(TABLE_NOTIF, null, contentValues)
        db.close()
        return result
    }

    // Get User Notifications
    fun getUserNotifications(email: String): List<Notification> {
        val notifList = ArrayList<Notification>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NOTIF WHERE $COL_NOTIF_USER = ? ORDER BY $COL_NOTIF_ID DESC"
        val cursor = db.rawQuery(selectQuery, arrayOf(email))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTIF_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_TITLE))
                val msg = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_MSG))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_DATE))
                val user = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_USER))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_TYPE))

                val notif = Notification(id, title, msg, date, user, type)
                notifList.add(notif)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notifList
    }

    // Insert Car
    fun addCar(car: Car): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_CAR_NAME, car.name)
        contentValues.put(COL_CAR_TYPE, car.type)
        contentValues.put(COL_CAR_PRICE, car.pricePerDay)
        contentValues.put(COL_CAR_STATUS, car.status)
        contentValues.put(COL_CAR_IMAGE, car.image)
        contentValues.put(COL_CAR_SEATS, car.seats)
        contentValues.put(COL_CAR_FUEL, car.fuelType)

        val result = db.insert(TABLE_CAR, null, contentValues)
        db.close()
        return result
    }

    // Delete Car
    fun deleteCar(carId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CAR, "$COL_CAR_ID=?", arrayOf(carId.toString()))
        db.close()
        return result
    }

    // Get All Available Cars
    fun getAllAvailableCars(): List<Car> {
        updateCarStatuses() // Sync status first
        val carList = ArrayList<Car>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CAR WHERE $COL_CAR_STATUS = 'Available'"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAR_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NAME))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_TYPE))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_PRICE))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_STATUS))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_IMAGE))
                
                // Use safe retrieval for new columns in case of partial migration issues (though we drop table)
                val seats = if (cursor.getColumnIndex(COL_CAR_SEATS) != -1) 
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_SEATS)) else "5 Seats"
                val fuel = if (cursor.getColumnIndex(COL_CAR_FUEL) != -1) 
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_FUEL)) else "Hybrid"

                val car = Car(id, name, type, price, status, image, seats, fuel)
                carList.add(car)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return carList
    }

    // Get All Cars (for Company Dashboard)
    fun getAllCars(): List<Car> {
        updateCarStatuses() // Sync status first
        val carList = ArrayList<Car>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CAR"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAR_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NAME))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_TYPE))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_PRICE))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_STATUS))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_IMAGE))
                
                val seats = if (cursor.getColumnIndex(COL_CAR_SEATS) != -1) 
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_SEATS)) else "5 Seats"
                val fuel = if (cursor.getColumnIndex(COL_CAR_FUEL) != -1) 
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_FUEL)) else "Hybrid"

                val car = Car(id, name, type, price, status, image, seats, fuel)
                carList.add(car)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return carList
    }
    // Get Customer Bookings
    fun getCustomerBookings(email: String): List<CustomerBooking> {
        updateBookingStatuses() // Sync status first
        val bookingList = ArrayList<CustomerBooking>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_BOOKING WHERE $COL_BOOK_CUST_EMAIL = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(email))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID))
                val carName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CAR_NAME))
                val carDetails = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CAR_DETAILS))
                val startDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_START))
                val endDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_END))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_LOC))
                val bookedOn = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DATE))
                val totalAmount = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TOTAL))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS))
                val custEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CUST_EMAIL))

                val booking = CustomerBooking(id, carName, carDetails, startDate, endDate, location, bookedOn, totalAmount, status, custEmail)
                bookingList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bookingList
    }

    // Get All Bookings (for Company)
    fun getAllBookings(): List<CustomerBooking> {
        updateBookingStatuses() // Sync status first
        val bookingList = ArrayList<CustomerBooking>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_BOOKING"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID))
                val carName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CAR_NAME))
                val carDetails = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CAR_DETAILS))
                val startDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_START))
                val endDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_END))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_LOC))
                val bookedOn = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DATE))
                val totalAmount = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TOTAL))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS))
                val custEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CUST_EMAIL))

                val booking = CustomerBooking(id, carName, carDetails, startDate, endDate, location, bookedOn, totalAmount, status, custEmail)
                bookingList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bookingList
    }
    // Get Customer Name by Email
    fun getCustomerName(email: String): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COL_CUST_NAME FROM $TABLE_CUSTOMER WHERE $COL_CUST_EMAIL=?", arrayOf(email))
        var name = "User"
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_NAME))
        }
        cursor.close()
        db.close()
        return name
    }

    // --- Dynamic Status Update Logic ---

    private fun updateBookingStatuses() {
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_BOOKING"
        val cursor = db.rawQuery(selectQuery, null)

        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
        val today = java.util.Calendar.getInstance().time

        if (cursor.moveToFirst()) {
            do {
                try {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID))
                    val startDateStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_START))
                    val endDateStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_END))
                    val currentStatus = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS))

                    val startDate = dateFormat.parse(startDateStr)
                    val endDate = dateFormat.parse(endDateStr)

                    var newStatus = currentStatus
                    if (startDate != null && endDate != null) {
                        if (today.after(endDate)) {
                            newStatus = "Completed"
                        } else if (today.after(startDate) || today == startDate) { // very rough "today" check
                             // In real app compare truncated dates
                             // If today is equal start or after start but before end
                            newStatus = "Active"
                        } else {
                            newStatus = "Confirmed"
                        }
                    }

                    if (newStatus != currentStatus && currentStatus != "Cancelled") {
                        val contentValues = ContentValues()
                        contentValues.put(COL_BOOK_STATUS, newStatus)
                        db.update(TABLE_BOOKING, contentValues, "$COL_BOOK_ID=?", arrayOf(id.toString()))

                        // Trigger "Booking Completed" Notification
                        if (newStatus == "Completed") {
                            // Fetch customer email for this booking
                            val custEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CUST_EMAIL))
                            val carName = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CAR_NAME))
                            
                            // Check if notification already exists (Deduplication)
                            // A simple check: Look for a notification with this title and carName in message for this user
                            // This prevents spamming if this function is called multiple times
                            val checkQuery = "SELECT * FROM $TABLE_NOTIF WHERE $COL_NOTIF_USER=? AND $COL_NOTIF_TITLE='Booking Completed' AND $COL_NOTIF_MSG LIKE '%$carName%'"
                            val dupCursor = db.rawQuery(checkQuery, arrayOf(custEmail))
                            val alreadyNotified = dupCursor.count > 0
                            dupCursor.close()

                            if (!alreadyNotified) {
                                val notifDate = dateFormat.format(today)
                                val msg = "Your trip with $carName has ended. We hope you had a safe journey!"
                                val cvNotif = ContentValues()
                                cvNotif.put(COL_NOTIF_TITLE, "Booking Completed")
                                cvNotif.put(COL_NOTIF_MSG, msg)
                                cvNotif.put(COL_NOTIF_DATE, notifDate)
                                cvNotif.put(COL_NOTIF_USER, custEmail)
                                cvNotif.put(COL_NOTIF_TYPE, "booking")
                                db.insert(TABLE_NOTIF, null, cvNotif)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }

    private fun updateCarStatuses() {
        val db = this.writableDatabase
        
        // Reset all to Available first (unless Maintenance)
        // A simpler approach: Check ACTIVE bookings
        // 1. Get List of associated car names/details that are currently ACTIVE
        val activeCars = ArrayList<String>() // Using Car Name + Details as key is shaky, but schema has separate tables without foreign keys
        // Ideally we should have stored Car ID in booking.
        // Since we only have Name + Type/Details, let's use Name as unique identifier for now (assuming unique names)
        
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
        val today = java.util.Calendar.getInstance().time
        
        // Read Bookings
        val cursorB = db.rawQuery("SELECT * FROM $TABLE_BOOKING", null)
        if (cursorB.moveToFirst()) {
            do {
                 try {
                    val carName = cursorB.getString(cursorB.getColumnIndexOrThrow(COL_BOOK_CAR_NAME))
                    val startDateStr = cursorB.getString(cursorB.getColumnIndexOrThrow(COL_BOOK_START))
                    val endDateStr = cursorB.getString(cursorB.getColumnIndexOrThrow(COL_BOOK_END))
                    
                    val startDate = dateFormat.parse(startDateStr)
                    val endDate = dateFormat.parse(endDateStr)
                    
                    if (startDate != null && endDate != null) {
                        // Check if today is within range
                        // start <= today <= end
                        // Using lenient comparison
                        if ((today.after(startDate) || isSameDay(today, startDate)) && 
                            (today.before(endDate) || isSameDay(today, endDate))) {
                             activeCars.add(carName)
                        }
                    }
                 } catch (e: Exception) { e.printStackTrace() }
            } while (cursorB.moveToNext())
        }
        cursorB.close()

        // Now Update Cars
        val cursorC = db.rawQuery("SELECT * FROM $TABLE_CAR", null)
        if (cursorC.moveToFirst()) {
            do {
                val id = cursorC.getInt(cursorC.getColumnIndexOrThrow(COL_CAR_ID))
                val name = cursorC.getString(cursorC.getColumnIndexOrThrow(COL_CAR_NAME))
                val currentStatus = cursorC.getString(cursorC.getColumnIndexOrThrow(COL_CAR_STATUS))

                var newStatus = currentStatus
                
                if (activeCars.contains(name)) {
                    newStatus = "Rented"
                } else {
                    // Only revert to Available if it was Rented. 
                    // If it was "Maintenance", keep it.
                    if (currentStatus == "Rented") {
                        newStatus = "Available"
                    }
                }

                if (newStatus != currentStatus) {
                    val cv = ContentValues()
                    cv.put(COL_CAR_STATUS, newStatus)
                    db.update(TABLE_CAR, cv, "$COL_CAR_ID=?", arrayOf(id.toString()))
                }
            } while (cursorC.moveToNext())
        }
        cursorC.close()
        db.close()
    }
    
    private fun isSameDay(date1: java.util.Date, date2: java.util.Date): Boolean {
        val fmt = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US)
        return fmt.format(date1) == fmt.format(date2)
    }

}
