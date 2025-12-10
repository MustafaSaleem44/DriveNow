<?php
include 'connection.php';

// Optional: Filter by status
$status = isset($_POST['status']) ? $_POST['status'] : '';

if ($status) {
    $query = "SELECT * FROM car WHERE status = '$status'";
} else {
    $query = "SELECT * FROM car";
}

$result = mysqli_query($con, $query);
$cars = [];

while ($row = mysqli_fetch_assoc($result)) {
    $cars[] = $row;
}

echo json_encode(["status" => "success", "data" => $cars]);
?>