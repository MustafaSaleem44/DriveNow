<?php
include 'connection.php';

$name = $_POST['name'];
$email = $_POST['email'];
$password = $_POST['password'];
$table = $_POST['table']; // 'customer' or 'company'

if (!$name || !$email || !$password || !$table) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

// Check if User Exists
$checkQuery = "SELECT * FROM $table WHERE email = '$email'";
$checkResult = mysqli_query($con, $checkQuery);

if (mysqli_num_rows($checkResult) > 0) {
    echo json_encode(["status" => "error", "message" => "Email already exists"]);
} else {
    $insertQuery = "INSERT INTO $table (name, email, password) VALUES ('$name', '$email', '$password')";
    if (mysqli_query($con, $insertQuery)) {
        echo json_encode(["status" => "success", "message" => "Signup successful"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Signup failed"]);
    }
}
?>