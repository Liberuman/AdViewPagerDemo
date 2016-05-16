package com.sxu.adviewpagerdemo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView button;
    private AdDialog dialog;
    private List<Drawable> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (TextView) findViewById(R.id.button);

        // 读取要显示的图片
        imageList.add(getResources().getDrawable(R.mipmap.icon1));
        imageList.add(getResources().getDrawable(R.mipmap.icon2));
        imageList.add(getResources().getDrawable(R.mipmap.icon3));
        imageList.add(getResources().getDrawable(R.mipmap.icon4));
        imageList.add(getResources().getDrawable(R.mipmap.icon5));
        imageList.add(getResources().getDrawable(R.mipmap.icon1));
        imageList.add(getResources().getDrawable(R.mipmap.icon2));
        imageList.add(getResources().getDrawable(R.mipmap.icon3));
        imageList.add(getResources().getDrawable(R.mipmap.icon4));
        imageList.add(getResources().getDrawable(R.mipmap.icon5));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new AdDialog(MainActivity.this, R.style.FullScreenDialogTheme, imageList);
                }

                dialog.show();
            }
        });
    }
}
