package com.atguigu.beijingnews.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by My on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private TextView textView;

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("左侧菜单");
    }
}
