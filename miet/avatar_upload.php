<?php

include_once 'config.php';

$file1 = $_POST['front'];
$file2 = $_POST['back'];


$data1 = base64_decode($file1);
$data2 = base64_decode($file2);


$sql = "INSERT INTO `avatar` (`front`, `back`)
VALUES (?,?)";

if($stmt = mysqli_prepare($conn,$sql)){ 

mysqli_stmt_bind_param($stmt, "ss",$data1,$data2); //binding the parameters    

$result =mysqli_stmt_execute($stmt);

if($result === FALSE){
    $response["success"] = 0;
   $response["error"] = 'mysqli query failed';
   $response["message"] = 'Something went wrong. Please contact golimits';
      echo json_encode($response);
   
   }
   else
   {
       
    $response["success"] = 1;
   $response["error"] = 'success';
   $response["message"] = 'Added Successfully.';
   echo json_encode($response);
   
   
   }
}
?>