<?php
include 'connection.php';

$id = $_POST['id'];

if (!$id) {
    echo json_encode(["status" => "error", "message" => "Missing ID"]);
    exit;
}

$query = "DELETE FROM car WHERE id = '$id'";

if (mysqli_query($con, $query)) {
    echo json_encode(["status" => "success", "message" => "Car deleted successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to delete car"]);
}
?>