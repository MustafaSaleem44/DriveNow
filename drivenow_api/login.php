<?php
include 'connection.php';

$email = $_POST['email'];
$password = $_POST['password'];
$table = $_POST['table']; // 'customer' or 'company'

if (!$email || !$password || !$table) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

$query = "SELECT * FROM $table WHERE email = '$email' AND password = '$password'";
$result = mysqli_query($con, $query);

if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    // Remove password from response
    unset($row['password']);
    echo json_encode(["status" => "success", "message" => "Login successful", "data" => $row]);
} else {
    echo json_encode(["status" => "error", "message" => "Invalid credentials"]);
}
?>