<?php
include 'connection.php';

$name = $_POST['name'];
$type = $_POST['type'];
$price = $_POST['price'];
$status = $_POST['status'];
$image = $_POST['image']; // Base64 string
$seats = $_POST['seats'];
$fuel = $_POST['fuel'];

if (!$name || !$price) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

// In a real app, you might upload the image to a folder and store the path. 
// For now, we store the Base64 string directly as requested/implemented in SQLite.
// NOTE: POST size limit in php.ini might need increasing for large Base64 images.

$query = "INSERT INTO car (name, type, price, status, image, seats, fuel) VALUES ('$name', '$type', '$price', '$status', '$image', '$seats', '$fuel')";

if (mysqli_query($con, $query)) {
    echo json_encode(["status" => "success", "message" => "Car added successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to add car: " . mysqli_error($con)]);
}
?>