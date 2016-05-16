package com.sxu.adviewpagerdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdDialog extends AlertDialog {

    private Context context;
    private ImageView closeIcon;
    private ImageView[] indicatorIcon;
    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private LinearLayout containLayout;

    private final float H_W = 7.0f / 5.0f;
    private Map<Integer, View> activityMap = new HashMap<Integer, View>();
    private List<Drawable> imageList;

    public AdDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AdDialog(Context context, int theme, List<Drawable> imageList) {
        super(context, theme);
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_layout);
        setCanceledOnTouchOutside(false);
        closeIcon = (ImageView)findViewById(R.id.close_icon);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        containLayout = (LinearLayout)findViewById(R.id.contain_layout);
        indicatorLayout = (LinearLayout)findViewById(R.id.indicator_layout);

        viewPager.setOffscreenPageLimit(imageList.size());
        viewPager.setAdapter(new MyPagerAdapter());
        if (imageList.size() > 1) {
            loadIndicator(imageList.size());
        }

        containLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 虽然一屏只显示三页，但是当我们滑动的过程中需要设置四页的缩放效果，因为左边的页面被滑出去之后，页面上将显示的是后面的三页
                View leftImage = activityMap.get(position - 1);
                View currentImage = activityMap.get(position);
                View rightImage = activityMap.get(position + 1);
                View rightImage2 = activityMap.get(position + 2);
                if (positionOffset > 0) {
                    float scaleAlpha1 = 1 - positionOffset / 4.0f;
                    float scaleAlpha2 = 0.75f + positionOffset / 4.0f;
                    // 设置页面滑动过程中的缩放效果
                    currentImage.setAlpha(scaleAlpha1);
                    currentImage.setScaleX(scaleAlpha1);
                    currentImage.setScaleY(scaleAlpha1);
                    if (leftImage != null) {
                        leftImage.setAlpha(scaleAlpha2);
                        leftImage.setScaleX(scaleAlpha2);
                        leftImage.setScaleY(scaleAlpha2);
                    }
                    if (rightImage != null) {
                        rightImage.setAlpha(scaleAlpha2);
                        rightImage.setScaleX(scaleAlpha2);
                        rightImage.setScaleY(scaleAlpha2);
                    }
                    if (rightImage2 != null) {
                        rightImage2.setAlpha(scaleAlpha1);
                        rightImage2.setScaleX(scaleAlpha1);
                        rightImage2.setScaleY(scaleAlpha1);
                    }
                } else {
                    // 设置页面初次启动时的缩放效果
                    if (leftImage != null && currentImage.getAlpha() == 1) {
                        leftImage.setAlpha(0.75f);
                        leftImage.setScaleX(0.75f);
                        leftImage.setScaleY(0.75f);
                    }
                    if (rightImage != null && currentImage.getAlpha() == 1) {
                        rightImage.setAlpha(0.75f);
                        rightImage.setScaleX(0.75f);
                        rightImage.setScaleY(0.75f);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                selectCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public int dpToPx(int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * ((float) dp)+0.5);
    }

    public int getScreenWidth() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels;
    }

    private void loadIndicator(int size) {
        indicatorIcon = new ImageView[size];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(8), dpToPx(8));
        params.leftMargin = dpToPx(3);
        params.rightMargin = dpToPx(3);
        for (int i = 0; i < size; i++) {
            indicatorIcon[i] = new ImageView(context);
            indicatorIcon[i].setLayoutParams(params);
            if (i == 0) {
                indicatorIcon[i].setBackgroundResource(R.drawable.indicator_selected);
            } else {
                indicatorIcon[i].setBackgroundResource(R.drawable.indicatorunselect);
            }
            indicatorLayout.addView(indicatorIcon[i]);
        }
    }

    /**
     * 设置当前选中页的选中状态（只有一页时不显示选中状态）
     * @param position 当前选中页的index
     */
    private void selectCurrentDot(int position) {
        if (imageList.size() > 1) {
            for (int i = 0; i < indicatorIcon.length; i++) {
                if (i == position) {
                    indicatorIcon[i].setBackgroundResource(R.drawable.indicator_selected);
                } else {
                    indicatorIcon[i].setBackgroundResource(R.drawable.indicatorunselect);
                }
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            RoundImageView imageView = new RoundImageView(context);
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = getScreenWidth() - dpToPx(140);
            params.height = (int)(params.width * H_W);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageDrawable(imageList.get(position));
            imageView.setTag(position);
            container.addView(imageView);
            activityMap.put(position, imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "触发了点击事件", Toast.LENGTH_SHORT).show();
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
