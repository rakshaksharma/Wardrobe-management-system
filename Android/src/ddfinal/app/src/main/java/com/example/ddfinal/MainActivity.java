package com.example.ddfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    Button move_upper, move_lower , resize_upper , resize_lower , add , avatar , flip , fav,share, fav_list;
    private String categories[] = {"casual", "formal", "festival", "marraige"};
    private ArrayList<String> images_data_binary = new ArrayList<>();
    categoryFragment cfp;
    ImageView upper, lower;
    int avatar_selectionId;
    Context context;
    ImageView avatar_imageView;
    avatarFragment dialogFragment;
    LinearLayout buttonHolder, buttonHolder2;
    private getdata object;
    int retrieved_product_list_size=5;
    int selected_product_id_upper,selected_product_id_lower=999;
    String selected_category;
    private String url = "http://192.168.43.249/miet/get_image.php";
    private Spinner spinner;
    private boolean imageupper_front_flag=true, imagelower_front_flag=true , avatar_front_image=true;



    ArrayList<String> images_data_front , images_data_back , avatar_images_frontList , avatar_images_backlist;


    int upper_front[],upper_back[],lower_front[],lower_back[];





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(arrayAdapter);
        avatar_imageView=(ImageView)findViewById(R.id.avatar_imageview);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);

        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selected_category = categories[position];
                        break;

                    case 1:

                        selected_category = categories[position];
                        break;

                    case 2:

                        selected_category = categories[position];
                        break;

                    case 3:

                        selected_category = categories[position];

                        break;


                    default:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        upper_front=new int[retrieved_product_list_size];
        lower_front = new int[retrieved_product_list_size];

        for(int i =0; i < 5;i++)
        {
            upper_front[i]=i;
        }


        for(int i =0; i <5;i++)
        {
            lower_front[i]=i+5;
        }



        upper = (ImageView) findViewById(R.id.upper);
        lower = (ImageView) findViewById(R.id.lower);
        add = (Button)findViewById(R.id.add);
        avatar=(Button)findViewById(R.id.avatar);
        buttonHolder=(LinearLayout)findViewById(R.id.button_holder);
        buttonHolder2=(LinearLayout)findViewById(R.id.button_holder2);
        flip=(Button)findViewById(R.id.flip);
        share=(Button)findViewById(R.id.share);
        fav_list=(Button)findViewById(R.id.fav_list);


        fav_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_collection fvc;
                Context c = getApplicationContext();
                fvc = new fav_collection(MainActivity.this);

                Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);

                fvc.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                fvc.show(ft, "dialog");

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        fav=(Button)findViewById(R.id.fav);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            new uploadFav(selected_product_id_upper,selected_product_id_lower,selected_category,"alpha", MainActivity.this);


            }
        });



        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  images_data_front=cfp.getImagesBinaryDataFront();
              //  images_data_back=cfp.getImagesBinaryDataBack();
                avatar_images_backlist=dialogFragment.getAvatarBackImages();
                avatar_images_frontList=dialogFragment.getAvatarFrontImages();



                if(avatar_front_image) {
                    avatar_imageView.setImageBitmap(get_imageBitmap(avatar_images_backlist.get(avatar_selectionId)));
                    avatar_front_image=false;
                }

                else if(avatar_front_image == false)
                {
                    avatar_imageView.setImageBitmap(get_imageBitmap(avatar_images_frontList.get(avatar_selectionId)));
                    avatar_front_image=true;
                }

                cfp.flipClothes();


            }
        });




        context = this.getApplicationContext();

        resize_upper=(Button)findViewById(R.id.resize_upper);



        resize_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upper.setOnTouchListener(null);
                upper.setOnTouchListener(new ptzHelper(context,upper));
            }
        });


        move_upper=(Button)findViewById(R.id.move_upper);
        move_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upper.setOnTouchListener(null);
                upper.setOnTouchListener(new dragHelper());

            }
        });


        resize_lower=(Button)findViewById(R.id.resize_lower);
        resize_lower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lower.setOnTouchListener(null);
                lower.setOnTouchListener(new ptzHelper(context,lower));

            }
        });


        move_lower=(Button)findViewById(R.id.move_lower);
        move_lower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lower.setOnTouchListener(null);
                lower.setOnTouchListener(new dragHelper());

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                cfp = new categoryFragment(MainActivity.this,selected_product_id_upper,selected_product_id_lower,selected_category,url,upper,lower );

                Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);

                cfp.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                cfp.show(ft, "dialog");




            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "upper : " +selected_product_id_upper + " lower " +selected_product_id_lower ,Toast.LENGTH_LONG ).show();

                dialogFragment = new avatarFragment(avatar_selectionId,avatar_imageView , MainActivity.this);

                Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);

                dialogFragment.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                dialogFragment.show(ft, "dialog");


            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        if(selected_product_id_lower==999)
            hide_layout(true);
    }

    void hide_layout(Boolean lower_selected)
    {
        if(lower_selected==true)
        {
            buttonHolder2.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonHolder2.setVisibility(View.VISIBLE);
        }


    }


    Bitmap get_imageBitmap(String image_data)
    {
        byte[] decodedString = Base64.decode(image_data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 10, 10, bitmap.getWidth() - 20, bitmap.getHeight() - 20);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    croppedBitmap, "Title", null);
            Uri imageUri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Select"));


        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


}



