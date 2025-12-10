package com.example.drivenow

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DriveNowApi {

    @FormUrlEncoded
    @POST("signup.php")
    fun signupUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("table") table: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("table") table: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("add_car.php")
    fun addCar(
        @Field("name") name: String,
        @Field("type") type: String,
        @Field("price") price: String,
        @Field("status") status: String,
        @Field("image") image: String,
        @Field("seats") seats: String,
        @Field("fuel") fuel: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("get_cars.php")
    fun getCars(
        @Field("status") status: String = ""
    ): Call<CarResponse>

    @FormUrlEncoded
    @POST("delete_car.php")
    fun deleteCar(
        @Field("id") id: Int
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("book_car.php")
    fun bookCar(
        @Field("car_name") carName: String,
        @Field("car_details") carDetails: String,
        @Field("start_date") startDate: String,
        @Field("end_date") endDate: String,
        @Field("location") location: String,
        @Field("booked_on") bookedOn: String,
        @Field("total_amount") totalAmount: String,
        @Field("status") status: String,
        @Field("customer_email") customerEmail: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("get_bookings.php")
    fun getBookings(
        @Field("email") email: String = ""
    ): Call<BookingResponse>

    @FormUrlEncoded
    @POST("add_notification.php")
    fun addNotification(
        @Field("title") title: String,
        @Field("message") message: String,
        @Field("date") date: String,
        @Field("user_email") userEmail: String,
        @Field("type") type: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("get_notifications.php")
    fun getNotifications(
        @Field("email") email: String
    ): Call<NotificationResponse>
}
