package com.example.ddfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imageModelArrayList;




     CustomAdapter(Context context, ArrayList<String> imageModelArrayList) {

        this.context = context;
//        this.imageModelArrayList.clear();
        this.imageModelArrayList = imageModelArrayList;

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, null, true);


            holder.iv = (ImageView) convertView.findViewById(R.id.image_list_holder);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }



        byte[] decodedString = Base64.decode(imageModelArrayList.get(position), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        // tvDragView.setImageBitmap(decodedByte);
        //holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.iv.setImageBitmap(decodedByte);
        return convertView;
    }



    private class ViewHolder {

        protected TextView tvname;
        private ImageView iv;

    }





}
