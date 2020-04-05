package com.example.ddfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

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

public class getdata {

    private Context context;
    private  String url , selected_category;
    ArrayList<String> images_data_front = new ArrayList<>();
    ArrayList<String> images_data_back = new ArrayList<>();

    getdata(Context context , String url , String selected_category)
    {
        this.context=context;
        this.url=url;
        this.selected_category = selected_category;
        getCategoryData();
    }


    getdata(Context context , String url)
    {
        this.context=context;
        this.url=url;
    }


    getdata()
    {

    }

    public void getCategoryData() {
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
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
                    images_data_front.add(jobj.getString("front"));
                    images_data_back.add(jobj.getString("back"));

                }
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


    ArrayList<String> get_frontImages()
    {
        return images_data_front;
    }


    ArrayList<String> get_backImages()
    {
        return images_data_back;
    }

}
