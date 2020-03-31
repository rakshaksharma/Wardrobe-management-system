package com.example.ddfinal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class categoryFragment extends DialogFragment {

    private ImageView imageView, front_upper, front_lower;
    ImageView b ,b1;
    int images[];
    boolean clothes_front_image=true , clothes_lower_image=true;
    int selected_product_id_upper,selected_product_id_lower;
    int upper_front[],lower_front[];
    boolean upper_selection =false, lower_selection =false;
    MainActivity ob;
    ListView cat_list;
    String selected_category,url;

    ArrayList<String> images_front_arraylist = new ArrayList<>();
    ArrayList<String> images_back_arraylist = new ArrayList<>();

    ArrayList<String> images_front_lower_arraylist = new ArrayList<>();
    ArrayList<String> images_back_lower_arraylist = new ArrayList<>();

    static boolean button_pressed;

    ArrayList<String> imagesDataBinary;

    public categoryFragment(MainActivity ob, int selected_product_id_upper, int selected_product_id_lower , int[] upper_front , int[] lower_front)
    {
        this.ob=ob;
        this.selected_product_id_lower=selected_product_id_lower;
        this.selected_product_id_upper=selected_product_id_upper;
        this.upper_front=upper_front;
        this.lower_front=lower_front;


    }


    public categoryFragment(MainActivity ob, int selected_product_id_upper, int selected_product_id_lower, String selected_category, String url, ImageView upper, ImageView lower)
    {

        this.front_lower=lower;
        this.front_upper=upper;
        this.ob=ob;
        this.url=url;
        this.selected_category=selected_category;
        this.selected_product_id_lower=selected_product_id_lower;
        this.selected_product_id_upper=selected_product_id_upper;



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
        return inflater.inflate(R.layout.categoryfragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Button upper_item = (Button)view.findViewById(R.id.upper_item);
        Button lower_item = (Button) view.findViewById(R.id.lower_item);

       cat_list = (ListView)view.findViewById(R.id.cat_list);


        upper_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CFUI "+url ,"  "+ selected_category);
                cat_list.invalidate();
                lower_selection=false;
                upper_selection=true;
                button_pressed=true; // true for upper
                getCategoryData(url,selected_category);

            }
        });



        lower_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cat_list.invalidate();
//                ArrayList<Integer> data = new ArrayList<>();
//
//                for (int i : lower_front)
//                {
//                    data.add(i);
//                }
//
//                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,data);
//                cat_list.setAdapter(arrayAdapter);
//                lower_selection=true;
//                upper_selection=false;
                Log.e("CFUI "+url ,"  "+ (selected_category+"_lower"));
                cat_list.invalidate();
                lower_selection=true;
                upper_selection=false;
            //    Toast.makeText(ob,"Lower selected",Toast.LENGTH_SHORT).show();
                String url = "http://192.168.43.249/miet/get_image_lower.php";
                button_pressed=false; //true for upper
                getCategoryData(url , (selected_category+"_lower"));


            }
        });


        cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(upper_selection)
                {
                    ob.selected_product_id_upper=position;
                    front_upper.setImageBitmap(get_imageBitmap(images_front_arraylist.get(position)));
                //    Toast.makeText(getContext(),"Upper Selected " + ob.selected_product_id_upper + "", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ob.selected_product_id_lower=position;
                    front_lower.setImageBitmap(get_imageBitmap(images_front_lower_arraylist.get(position)));

                  //  Toast.makeText(getContext(),"Lower " + ob.selected_product_id_lower + "", Toast.LENGTH_LONG).show();
                }

            }
        });



    }


    public void flipClothes()
    {

        if(clothes_front_image) {
            if(images_back_arraylist.size()<0)
            {
                Toast.makeText(ob,"Please select upper item", Toast.LENGTH_SHORT).show();

            }
            else
            {
                try
                {
                    front_upper.setImageBitmap(get_imageBitmap(images_back_arraylist.get(selected_product_id_upper)));
                    clothes_front_image=false;
                }

                catch (Exception e)
                {

                }
            }

        }

        else if(clothes_front_image == false)
        {
            if(images_front_arraylist.size()<0)
            {
                Toast.makeText(ob,"Please select upper item", Toast.LENGTH_SHORT).show();

            }
            else
            {
                try {

                    front_upper.setImageBitmap(get_imageBitmap(images_front_arraylist.get(selected_product_id_upper)));
                    clothes_front_image=true;
                }
                catch (Exception e)
                {
                    Toast.makeText(ob,"Please select upper item", Toast.LENGTH_SHORT).show();

                }

            }

        }


        if(clothes_lower_image) {
            if(images_back_lower_arraylist.size()<0)
            {
                Toast.makeText(ob,"Please select lower item", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    front_lower.setImageBitmap(get_imageBitmap(images_back_lower_arraylist.get(0)));
                    clothes_lower_image = false;
                }
                catch (Exception e)
                {
                    Toast.makeText(ob,"Please select lower item", Toast.LENGTH_SHORT).show();

                }
            }
        }

        else if(clothes_lower_image == false)
        {
            if(images_front_lower_arraylist.size()<0)
            {
                Toast.makeText(ob,"Please select upper item", Toast.LENGTH_SHORT).show();

            }
            else {
                try {
                    front_lower.setImageBitmap(get_imageBitmap(images_front_lower_arraylist.get(0)));
                    clothes_lower_image = true;
                }
                catch (Exception e)
                {
                    Toast.makeText(ob,"Please select upper item", Toast.LENGTH_SHORT).show();

                }
            }


        }

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



    public void getCategoryData(String url , final String selected_category) {
        Log.e("Fetching data -----",url+" ---- " +selected_category);
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
           //     Toast.makeText(ob , "Button pressed " + Boolean.toString(button_pressed), Toast.LENGTH_SHORT).show();
                if(button_pressed)
                {
                    Log.d("Success", "" + success);
                    JSONArray payload = jsonObject.getJSONArray("mydata");
                    Log.e("PAYLOAD SIZE" , payload.length()+" -----------");
                    images_front_arraylist.clear();
                    images_back_arraylist.clear();
                    for (int i =0 ; i < payload.length() ; i++)
                    {
                        JSONObject jobj = payload.getJSONObject(i);

                        // data_size=data_size+i;
                        images_front_arraylist.add(jobj.getString("front"));
                        images_back_arraylist.add(jobj.getString("back"));



                    }


                    CustomAdapter customAdapter = new CustomAdapter(getContext(),images_front_arraylist);
                    Log.e("IMF SIZE ",images_front_arraylist.size()+"");
//                customAdapter.clearData();
                    customAdapter.notifyDataSetChanged();
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

                else
                    {

                        Log.d("Success", "" + success);
                        JSONArray payload = jsonObject.getJSONArray("mydata");
                        Log.e("PAYLOAD SIZE" , payload.length()+" -----------");
                        images_front_lower_arraylist.clear();
                        images_back_lower_arraylist.clear();
                        for (int i =0 ; i < payload.length() ; i++)
                        {
                            JSONObject jobj = payload.getJSONObject(i);

                            // data_size=data_size+i;
                            images_front_lower_arraylist.add(jobj.getString("front"));
                            images_back_lower_arraylist.add(jobj.getString("back"));



                        }


                        CustomAdapter customAdapter = new CustomAdapter(getContext(),images_front_lower_arraylist);
                        Log.e("IMF SIZE ",images_front_lower_arraylist.size()+"");
//                customAdapter.clearData();
                        customAdapter.notifyDataSetChanged();
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

            }
        } catch (JSONException je) {
            Log.d("JSONError", "Unable to parse response\n" + je);

        }

        return null;

    }








    ArrayList<String> getImagesBinaryDataFront()
    {
        return  images_front_arraylist;
    }



    ArrayList<String> getImagesBinaryDataBack()
    {

        return  images_back_arraylist;
    }

}

