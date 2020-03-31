package com.example.ddfinal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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

public class fav_collection extends DialogFragment {

    ArrayList<String> imageOne = new ArrayList<>();
    ArrayList<String> imageTwo = new ArrayList<>();
    ArrayList<String> likes = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    private String url = "http://192.168.43.249/miet/get_fav.php";

    ListView cat_list;
    MainActivity ob;


    fav_collection(MainActivity ob)
    {
        this.ob=ob;
        //this.c=c;
        getCategoryData();
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
        return inflater.inflate(R.layout.fav_collection, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        cat_list = (ListView)view.findViewById(R.id.listFav);


    //    upper_item.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View v) {
//                Log.e("CFUI "+url ,"  "+ selected_category);
//                cat_list.invalidate();
//                lower_selection=false;
//                upper_selection=true;
//                button_pressed=true; // true for upper
//                getCategoryData(url,selected_category);

//            }
//        });

    }


    public void getCategoryData() {
        Log.e("Fetching data -----",url+" ---- ");
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
                params.put("category", "fav_users"); // dummy value

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

                    Log.d("Success", "" + success);
                    JSONArray payload = jsonObject.getJSONArray("mydata");
                    Log.e("PAYLOAD SIZE" , payload.length()+" -----------");
                    imageOne.clear();
                    imageTwo.clear();
                    for (int i =0 ; i < payload.length() ; i++)
                    {
                        JSONObject jobj = payload.getJSONObject(i);

                        // data_size=data_size+i;
                        imageOne.add(jobj.getString("imageOne"));
                        imageTwo.add(jobj.getString("imageTwo"));
                        likes.add((jobj.getString("likes")));
                        category.add((jobj.getString("category")));




                    }


                    custom_adapter_fav customAdapter = new custom_adapter_fav(ob.getApplicationContext(),imageOne,imageTwo,likes,category);
                    Log.e("IMF SIZE ",imageOne.size()+"");
                    customAdapter.notifyDataSetChanged();
                    cat_list.setAdapter(customAdapter);


            }
        } catch (JSONException je) {
            Log.d("JSONError", "Unable to parse response\n" + je);

        }

        return null;

    }



}
