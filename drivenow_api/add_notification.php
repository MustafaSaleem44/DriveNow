<?php
include 'connection.php';

$title = $_POST['title'];
$message = $_POST['message'];
$date = $_POST['date'];
$user_email = $_POST['user_email'];
$type = $_POST['type'];

if (!$user_email || !$title) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit;
}

$query = "INSERT INTO notification (title, message, date, user_email, type) VALUES ('$title', '$message', '$date', '$user_email', '$type')";

if (mysqli_query($con, $query)) {
    echo json_encode(["status" => "success", "message" => "Notification added"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to add notification"]);
}
?>