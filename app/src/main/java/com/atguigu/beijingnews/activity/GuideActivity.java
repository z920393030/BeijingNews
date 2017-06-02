package com.atguigu.beijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point)
    LinearLayout llPoint;
    @InjectView(R.id.activity_guide)
    RelativeLayout activityGuide;
    @InjectView(R.id.iv_point_red)
    ImageView ivPointRed;
    private ArrayList<ImageView> imageViews;
    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private float leftMargin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);

        initData();

        viewpager.setAdapter(new MyPagerAdapter());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        ivPointRed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivPointRed.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                leftMargin = llPoint.getChildAt(1).getLeft() - llPoint.getChildAt(0).getLeft();
            }
        });
    }

    private void initData() {

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(GuideActivity.this,10), DensityUtil.dip2px(GuideActivity.this,10));
            if (i != 0) {
                params.leftMargin =DensityUtil.dip2px(GuideActivity.this,10);
            }
            point.setLayoutParams(params);
            point.setImageResource(R.drawable.point_normal);

            llPoint.addView(point);
        }


    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
        CacheUtils.putBoolean(this,"start_main",true);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            float left = leftMargin * (positionOffset + position);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPointRed.getLayoutParams();
            params.leftMargin = (int) left;
            ivPointRed.setLayoutParams(params);

        }

        @Override
        public void onPageSelected(int position) {

            if (position == imageViews.size() - 1) {
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                btnStartMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
