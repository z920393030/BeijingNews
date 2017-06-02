package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

/**
 * Created by My on 2017/6/2.
 */

public class BasePager {
    public Context context;
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;

    public FrameLayout fl_content;


    public BasePager(Context context){
        this.context = context;
        rootView = View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) rootView.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
    }


    public void initData(){

    }
}
