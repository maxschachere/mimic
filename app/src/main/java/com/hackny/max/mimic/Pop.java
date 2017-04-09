package com.hackny.max.mimic;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Arjun on 4/9/17.
 */

public class Pop extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        TextView fashionTrends = (TextView) findViewById(R.id.trends);
        String fashionQuote = getIntent().getStringExtra("FASHION_QUOTE");
        fashionTrends.setText(fashionQuote);
    }

}
