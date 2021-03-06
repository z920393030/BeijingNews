package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;

/**
 * Created by My on 2017/6/2.
 */

public class BasePager {
    public Context context;
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;
    public ImageButton ib_switch_list_grid;

    public FrameLayout fl_content;


    public BasePager(final Context context){
        this.context = context;
        rootView = View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) rootView.findViewById(R.id.ib_menu);
        ib_switch_list_grid = (ImageButton) rootView.findViewById(R.id.ib_switch_list_grid);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).getSlidingMenu().toggle();
            }
        });
    }


    public void initData(){

    }
}
