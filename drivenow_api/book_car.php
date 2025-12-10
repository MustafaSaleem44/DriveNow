<?php
include 'connection.php';

$car_name = $_POST['car_name'];
$car_details = $_POST['car_details'];
$start_date = $_POST['start_date'];
$end_date = $_POST['end_date'];
$location = $_POST['location'];
$booked_on = $_POST['booked_on'];
$total_amount = $_POST['total_amount'];
$status = $_POST['status'];
$customer_email = $_POST['customer_email'];

if (!$customer_email || !$car_name) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

$query = "INSERT INTO booking (car_name, car_details, start_date, end_date, location, booked_on, total_amount, status, customer_email) VALUES ('$car_name', '$car_details', '$start_date', '$end_date', '$location', '$booked_on', '$total_amount', '$status', '$customer_email')";

if (mysqli_query($con, $query)) {
    echo json_encode(["status" => "success", "message" => "Booking successful"]);
} else {
    echo json_encode(["status" => "error", "message" => "Booking failed: " . mysqli_error($con)]);
}
?>