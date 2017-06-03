package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnews.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews.detailpager.VoteMenuDetailPager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by My on 2017/6/2.
 */

public class NewsPager extends BasePager {
    private List<NewsCenterBean.DataBean> datas;
    private List<MenuDetailBasePager> basePagers;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        tv_title.setText("新闻");

        ib_menu.setVisibility(View.VISIBLE);

        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        fl_content.addView(textView);

        getDataFromNet();
    }

    private void getDataFromNet() {
        String url = ConstantUtils.NEWSCENTER_PAGER_URL;

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","请求失败=="+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","请求成功=="+response);
                        processData(response);
                    }
                });
    }

    private void processData(String json) {
        NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class);
        Log.e("TAG","解析成功了哦=="+ newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();
        MainActivity mainActivity = (MainActivity) context;

        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(context));
        basePagers.add(new TopicMenuDetailPager(context));
        basePagers.add(new PhotosMenuDetailPager(context));
        basePagers.add(new InteractMenuDetailPager(context));
        basePagers.add(new VoteMenuDetailPager(context));

        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(datas);


    }

    public void swichPager(int prePosition) {
        MenuDetailBasePager basePager = basePagers.get(prePosition);
        View rootView = basePager.rootView;
        fl_content.removeAllViews();
        fl_content.addView(rootView);
        basePager.initData();
    }
}
