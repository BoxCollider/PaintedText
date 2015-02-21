package com.boxcollider.paintedtext;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PaintedTextDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painted_text_demo);

        //Reference layout that will hold the views
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);

        //Create PaintedText with BitmapDrawable
        BitmapDrawable drawableGreen=(BitmapDrawable) getResources().getDrawable(R.drawable.cellular_green);
        PaintedText ninja = new PaintedText(this,"NEW DAY",drawableGreen ,50,"kinifed.ttf");
        parent.addView(ninja);

        addSpace(parent);

        //Create PaintedText with image resource ID
        PaintedText pirates = new PaintedText(this,"PIRATES", R.drawable.cellular_red,50,"PirataOne.ttf");
        parent.addView(pirates);

        addSpace(parent);

        //Create PaintedText with Bitmap
        final Bitmap bitmapWhite = BitmapFactory.decodeResource(getResources(),R.drawable.cellular_white);
        PaintedText flower = new PaintedText(this,"FLOWER", bitmapWhite,50,"chocolate.ttf");
        parent.addView(flower);

        addSpace(parent);

    }

    private void addSpace(ViewGroup parent){
        getLayoutInflater().inflate(R.layout.spacer,parent,true);
    }

}
