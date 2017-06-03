package com.atguigu.beijingnews;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSlidingMenu();
        initFragment();

    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_left,new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main,new ContentFragment(), MAIN_TAG);
        ft.commit();
    }

    private void initSlidingMenu() {
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);


        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        slidingMenu.setBehindOffset(200);
    }

    public LeftMenuFragment getLeftMenuFragment() {

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_TAG);
    }

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG);
    }
}
