<?php

$email =$_POST['Email'];
$password =$_POST['Password'];

if($email=="amit.74-cse-16@mietjammu.in")
{
    if($password=="password")
    {
        echo "hello and welcome to dashboard";
        header('location:index.html'); 
    }
}
else
{
 echo "Enter Correct value";
header('location:index.html');
}
?> 