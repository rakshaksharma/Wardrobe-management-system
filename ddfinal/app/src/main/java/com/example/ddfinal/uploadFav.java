package com.example.ddfinal;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class uploadFav {

    private int idfront, idback ;
    private String  occasion , username;
    MainActivity ob;
    String url ="http://192.168.43.249/miet/fav_upload.php";



    uploadFav( int idfront , int idback , String occasion , String username , MainActivity ob)
    {
        this.ob=ob;
        this.idfront=idfront;
        this.idback=idback;
        this.occasion=occasion;
        this.username=username;
        uploadFavFunction();
    }




    public void uploadFavFunction() {
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
                params.put("idupper", Integer.toString(idfront));
                params.put("idlower", Integer.toString(idback));
                params.put("occasion",occasion);
                params.put("username",username);


                return params;
            }
        };

        requestQueue.add(stringRequest);


    }

    public HashMap<String, String> parseJson(String json) {

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
                Toast.makeText(ob.getApplicationContext(),"Your fav has been added ", Toast.LENGTH_LONG).show();

            }
        } catch (JSONException je) {
            Log.d("JSONError", "Unable to parse response\n" + je);

        }

        return null;

    }


}
