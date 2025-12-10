<?php
$host = "localhost";
$user = "root";
$pass = "";
$db = "drivenow_db";

$con = mysqli_connect($host, $user, $pass, $db);

if (!$con) {
    die(json_encode(["status" => "error", "message" => "Database Connection Error"]));
}
?>
