package com.example.ddfinal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class avatarFragment extends DialogFragment {




    int avatar_id;
    ImageView avatar;

    MainActivity ob;
    ListView cat_list;
    String selected_category="avatar";
    private String url = "http://192.168.43.249/miet/get_avatar.php";

    ArrayList<String> avatar_front = new ArrayList<>();
    ArrayList<String> avatar_back = new ArrayList<>();
    Bitmap avatar_gallery_front, avatar_gallery_back;

    ArrayList<String> imagesDataBinary;

    public avatarFragment(int avatar_id, ImageView imageView , MainActivity ob)
    {
        this.ob=ob;
        this.avatar_id=avatar_id;
        this.avatar=imageView;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.avatarfragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button gallery = (Button)view.findViewById(R.id.gallery_uploadfront);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery_upload();

            }
        });

        Button camera = (Button) view.findViewById(R.id.gallery_uploadback);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gallery_upload_back();
            }
        });



       Button upload = (Button) view.findViewById(R.id.upload);


        final ImageView im1 = (ImageView)view.findViewById(R.id.im1);
        final ImageView im2 =(ImageView) view.findViewById(R.id.im2);

       upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.e("front ACFG " , avatar_gallery_front+"");
               Log.e("front ACBG " , avatar_gallery_back+"");

               Drawable d1 = new BitmapDrawable(getResources(), avatar_gallery_front);
               Drawable d2 = new BitmapDrawable(getResources(), avatar_gallery_back);


               im1.setImageDrawable(d1);
               im2.setImageDrawable(d2);

               new uploadAvatar(avatar_gallery_front,avatar_gallery_back,ob);


           }
       });

        getCategoryData();

        cat_list = (ListView)view.findViewById(R.id.cat_list);




        cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ob.avatar_selectionId=position;
                    avatar.setImageBitmap(get_imageBitmap(avatar_front.get(position)));
                    Toast.makeText(getContext(),"Upper Selected " + ob.avatar_selectionId + "", Toast.LENGTH_LONG).show();


            }
        });



    }


    Bitmap get_imageBitmap(String image_data)
    {
        byte[] decodedString = Base64.decode(image_data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    public void getCategoryData() {
        RequestQueue requestQueue = VolleySingleton.getInstance(ob.getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonArray) {
                        Log.d("fetching data in", jsonArray);
                        parseJson(jsonArray);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error", "" + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", selected_category);

                return params;
            }
        };

        requestQueue.add(stringRequest);


    }


    public HashMap<String, String> parseJson(String json) {
        int data_size=0;
        JSONObject jsonObject = null;
        Log.e("parseJSON", "" + json);
        try {
            jsonObject = new JSONObject(json);
            int success = jsonObject.getInt("success");
            if (success == 0) {
                Log.d("JSONError", "No response form Server");

            } else {
                //images_data_back.clear();
                //images_data_front.clear();

                Log.d("Success", "" + success);
                JSONArray payload = jsonObject.getJSONArray("mydata");
                Log.e("PAYLOAD SIZE" , payload.length()+" -----------");

                for (int i =0 ; i < payload.length() ; i++)
                {
                    JSONObject jobj = payload.getJSONObject(i);

                    // data_size=data_size+i;
                    avatar_front.add(jobj.getString("front"));
                    avatar_back.add(jobj.getString("back"));



                }


                CustomAdapter customAdapter = new CustomAdapter(getContext(),avatar_front);
                Log.e("IMF SIZE ",avatar_front.size()+"");

                cat_list.setAdapter(customAdapter);
//                Log.e("Item recieved ----" , data_size+" ");
//                JSONObject jobj = payload.getJSONObject(0);
//
//                String img_data_front = jobj.getString("front");
//                String img_data_back = jobj.getString("back");
//                Log.e("response-front", img_data_front);
//                Log.e("response-back", img_data_back);


                //byte[] decodedString = Base64.decode(img_data_front, Base64.DEFAULT);
                //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                // tvDragView.setImageBitmap(decodedByte);

            }
        } catch (JSONException je) {
            Log.d("JSONError", "Unable to parse response\n" + je);

        }

        return null;

    }

    ArrayList<String> getAvatarFrontImages()
    {
        return  avatar_front;
    }



    ArrayList<String> getAvatarBackImages()
    {
        return  avatar_back;
    }


    void camera_upload()
    {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
    }


    void gallery_upload()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }


    void gallery_upload_back()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 2);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");

//                    Uri selectedImage = imageReturnedIntent.getData();
//                    Bitmap bitmap=null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(context, selectedImage);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                    avatar.setBackground(d);
                }

                break;
            case 1:
                //gallery upload
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap=null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ob.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Drawable d = new BitmapDrawable(getResources(), bitmap);
                    avatar_gallery_front=bitmap;
                }
                break;



            case 2:
                //gallery upload
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap=null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ob.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                   // Drawable d = new BitmapDrawable(getResources(), bitmap);
                    avatar_gallery_back=bitmap;
                }
                break;
        }
    }




}
