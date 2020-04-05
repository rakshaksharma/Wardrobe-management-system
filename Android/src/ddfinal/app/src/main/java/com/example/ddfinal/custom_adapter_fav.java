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

public class custom_adapter_fav extends BaseAdapter {

    private Context context;
    private ArrayList<String> imageOne;
    private ArrayList<String> imageTwo;
    private ArrayList<String> likes;
    private ArrayList<String> category;




    custom_adapter_fav(Context context, ArrayList<String> imageOne , ArrayList<String> imageTwo,ArrayList<String> likes , ArrayList<String> categories)
    {

        this.context = context;
//        this.imageModelArrayList.clear();
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.likes=likes;
        this.category=categories;

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
        return imageOne.size();
    }

    @Override
    public Object getItem(int position) {
        return imageOne.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        custom_adapter_fav.ViewHolder holder;

        if (convertView == null) {
            holder = new custom_adapter_fav.ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_fav, null, true);


            holder.im1 = (ImageView) convertView.findViewById(R.id.image_one);
            holder.im2 = (ImageView) convertView.findViewById(R.id.image_two);
            holder.likes = (TextView) convertView.findViewById(R.id.textLikes);
            holder.categoryText = (TextView) convertView.findViewById(R.id.textCategory);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (custom_adapter_fav.ViewHolder)convertView.getTag();
        }



        byte[] decodedString = Base64.decode(imageOne.get(position), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        byte[] decodedString2 = Base64.decode(imageTwo.get(position), Base64.DEFAULT);
        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);



        holder.im1.setImageBitmap(decodedByte);
        holder.im2.setImageBitmap(decodedByte2);
        holder.likes.setText(likes.get(position));
        holder.categoryText.setText(category.get(position));
        return convertView;
    }



    private class ViewHolder {

        protected TextView likes,categoryText;
        private ImageView im1  ,im2;


    }





}

