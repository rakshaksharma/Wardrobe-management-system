package com.example.ddfinal;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dell on 2/13/2017.
 */



public class volleyconnect implements Parcelable
{
      ArrayList<HashMap<String,String>> mydata = new ArrayList<>();
    String flag="check ";


    protected volleyconnect(Parcel in)
    {

    }

    public static final Creator<volleyconnect> CREATOR = new Creator<volleyconnect>() {
        @Override
        public volleyconnect createFromParcel(Parcel in) {
            return new volleyconnect(in);
        }

        @Override
        public volleyconnect[] newArray(int size) {
            return new volleyconnect[size];
        }
    };

    ArrayList<HashMap<String,String>> getDataStructure(volleyconnect vc)
    {
        return vc.mydata;
    }

   volleyconnect()
    {

    }

    JSONObject req(String param1, String param2)
    {
        JSONObject j=new JSONObject();
        try {
            j.put("param1", param1);
            j.put("param2", param2);
        }

        catch (JSONException je)
        {

        }
        return j;
    }

      volleyconnect connect(Context c,String data_url,String param1, String param2 )
    {
       // requestedData = null;
        Log.e("connecting now " , "for data");

        RequestQueue queue = VolleySingleton.getInstance(c).getRequestQueue();
        //  queue.getCache().clear();
        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, data_url,req(param1,param2),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("fetching data", response.toString());
                        getData(response);


                    }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }



        })
        ;







        queue.add(jsonObjectRequest);


        return this;
    }


     void getData(JSONObject jsonr)
    {
        // JSONObject jsonresponse = jsonr.getJSONObject("details");
        HashMap<String,String> dataHolder =null;


        String em = "";
        JSONObject jsonresponse = null;
        try {
            int success = jsonr.getInt("success");

            if (success == 1) {
                JSONArray jarray = jsonr.getJSONArray("products");

                for (int i = 0; i < jarray.length(); i++) {
                    try {
                        dataHolder= new HashMap<>();
                        jsonresponse = jarray.getJSONObject(i);
                        dataHolder.put("company_contact",jsonresponse.getString("company_contact"));
                        dataHolder.put("address_line_1",jsonresponse.getString("address_line_1"));
                        dataHolder.put("discount",jsonresponse.getString("discount"));


                    mydata.add(dataHolder);

                    }

                    catch (JSONException je)
                    {
                        Log.e("JSON Exception caught" ,je.toString());
                         }




                }

            }
            else
            {
                Log.e("no data","from server");
            }




        }
        catch(Exception e)
        {
            Log.e("Exception" ,""+e);
       //     Toast.makeText(this, " no data from server",Toast.LENGTH_SHORT ).show();
        }
     //   return mydata;
       // return mydata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this,i);

    }

    synchronized static volleyconnect getConnected(volleyconnect connectionObject)
    {
        if(connectionObject== null )
        {
            connectionObject= new volleyconnect();
        }

        return connectionObject;
    }
}




