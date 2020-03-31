package com.example.ddfinal;

import android.graphics.Bitmap;
import android.util.Log;

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

public class uploadAvatar {
    Bitmap front,back ;
    MainActivity ob;
    String url ="http://192.168.43.249/miet/avatar_upload.php";

    uploadAvatar(Bitmap front , Bitmap back , MainActivity ob)
    {
        this.ob=ob;
        this.front=front;
        this.back=back;
        uploadAvatar();

    }


    public void uploadAvatar() {
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
                params.put("front", (Base64.getEncoder().encodeToString(getImageByte(front))));
                params.put("back", (Base64.getEncoder().encodeToString(getImageByte(back))));


                return params;
            }
        };

        requestQueue.add(stringRequest);


    }

    byte[] getImageByte(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArray;
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




            }
        } catch (JSONException je) {
            Log.d("JSONError", "Unable to parse response\n" + je);

        }

        return null;

    }

}
