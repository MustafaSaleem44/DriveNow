<?php
include 'connection.php';

$email = $_POST['email'];

if (!$email) {
    echo json_encode(["status" => "error", "message" => "Missing email"]);
    exit;
}

$query = "SELECT * FROM notification WHERE user_email = '$email' ORDER BY id DESC";
$result = mysqli_query($con, $query);
$notifs = [];

while ($row = mysqli_fetch_assoc($result)) {
    $notifs[] = $row;
}

echo json_encode(["status" => "success", "data" => $notifs]);
?>