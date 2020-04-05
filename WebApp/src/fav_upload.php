<?php

include_once 'config.php';

$response["mydata"] = array();
 $response["success"] = 0;
 $response["error"] = 'error';
 $response["message"] = 'failure';


$idfront = $_POST['idupper'];
$idback = $_POST['idlower'];
$occasion= $_POST['occasion'];
$username= $_POST['username'];

$col='likes';
$table='fav_users';
$likes=1;
$row1 ='id-front';
$row2 ='id-back';
$row3 = 'occasion';

$query = "SELECT * FROM $table where `id-upper`=$idfront AND `id-lower`=$idback AND `occasion`='casual' ";

if ($result=mysqli_query($conn,$query))
//main if
{
   if(mysqli_num_rows($result) > 0)
      {
      $query1=("UPDATE $table SET likes=likes+1");
      mysqli_query($conn,$query1);
      $response["success"] = 1;
      $response["error"] = 'exist';
      $response["message"] = 'likes increased';
      echo json_encode($response);

   }
   else
   {

      //upload data
      $sql = "INSERT INTO $table (`id-upper`, `id-lower`,`occasion`,`likes`,`username`)
      VALUES (?,?,?,?,?)";

if($stmt = mysqli_prepare($conn,$sql)){ 

   mysqli_stmt_bind_param($stmt, "sssss",$idfront,$idback,$occasion,$likes,$username); //binding the parameters    
   
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
   }
   

} 
// end main if

else
//main else
{

   echo json_encode($response);

} 
//main else



?>