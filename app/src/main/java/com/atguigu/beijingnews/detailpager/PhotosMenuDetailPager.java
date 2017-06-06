package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.newsbeijing_library.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by My on 2017/6/3.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    android.support.v7.widget.RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    private TextView textView;
    private String url;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = ConstantUtils.BASE_URL + dataBean.getUrl();
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==" + response);
                        processData(response);

                    }


                });
    }

    private void processData(String json) {

    }
}
