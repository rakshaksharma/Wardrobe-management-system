<?php

include_once 'config.php';
   
$category = "fav_users"; //$_POST["category"]; 

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
            

//            $product["imageOne"] = base64_encode($row["front"]);
//            $product["imageTwo"] = base64_encode($row["front"]);

            $itemOne = $row["id-upper"];
            $itemTwo = $row["id-lower"];
            $likes = $row["likes"];
            $category_option = $row["occasion"];


            // echo $itemOne;
            // echo $itemTwo;
            // echo $likes;
            // echo $category_option;

            //$query = "SELECT * FROM $table where `id-upper`=$idfront AND `id-lower`=$idback AND `occasion`='casual' ";

            $query2 = "SELECT * FROM $category_option where `id` = $itemOne ";



            if ($result=mysqli_query($conn,$query2))
            {
          
             if(mysqli_num_rows($result) > 0)
              {
          
              while ($row = mysqli_fetch_array($result)){

                    $product["imageOne"] = base64_encode($row["front"]);
              
                }// while

            }
            
            else{
          //  echo "no image found ---------------------------------";
            }
                // if
        }//if


        $category_option2="casual_lower";
        $query3 = "SELECT * FROM $category_option2 where `id` = $itemTwo ";



        if ($result=mysqli_query($conn,$query3))
        {
      
         if(mysqli_num_rows($result) > 0)
          {
      
          while ($row = mysqli_fetch_array($result)){

                $product["imageTwo"] = base64_encode($row["front"]);
          
            }// while

        }// if
    }//if

         $product["likes"] = $likes;
         $product["category"] =$category_option;



            

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
