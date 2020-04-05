package com.example.ddfinal;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class ptzHelper extends MainActivity implements View.OnTouchListener  {

    private ScaleGestureDetector scaleGesture;
    private float mScaleFactor = 1.0f;
    private ImageView image;




    private Context context;


    ptzHelper(Context context , ImageView imageView)
    {
        this.image=imageView;
        this.context=context;
        scaleGesture = new ScaleGestureDetector(context, new ScaleListener());
        Log.e("Constructor ---------","init");

    }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("Constructor ---------","m1");
            onTouchEvent(event);
            return true;
         }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            scaleGesture.onTouchEvent(event);
            Log.e("Constructor ---------","m2");

            return true;
        }




        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                Log.e("Constructor ---------","m3");

                mScaleFactor *= scaleGestureDetector.getScaleFactor();
                mScaleFactor = Math.max(0.1f,
                        Math.min(mScaleFactor, 10.0f));
                image.setScaleX(mScaleFactor);
                image.setScaleY(mScaleFactor);
                return true;

            }
        }








}
