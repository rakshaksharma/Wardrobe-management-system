<?php

   include_once 'config.php';
   
$category = $_POST["category"];//'casual_lower'; 

 $query = "SELECT * FROM $category ";

 $response["mydata"] = array();
 $response["success"] = 0;
 $response["error"] = 'error';
 $response["message"] = 'failure';


 if ($result=mysqli_query($conn,$query))
  {

   if(mysqli_num_rows($result) > 0)
    {

    while ($row = mysqli_fetch_array($result)){

            $product = array();
            
            $product["front"] = base64_encode($row["front"]);
            $product["back"] = base64_encode($row["back"]);

            array_push($response["mydata"], $product);
             


        }

      $response["success"] = 1;
      $response["error"] = 'exist';
      $response["message"] = 'Front and back pic both Found';
      echo json_encode($response);    }
  else{
    $response["success"] = 0;
    $response["error"] = 'no details';
    $response["message"] = 'failure';
    echo json_encode($response); 
 
      }
    }

    else{
        echo json_encode($response);
    }

    
    ?>
