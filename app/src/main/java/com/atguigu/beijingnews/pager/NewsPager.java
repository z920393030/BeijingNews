package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        String saveJson = CacheUtils.getString(context, ConstantUtils.NEWSCENTER_PAGER_URL);

        if(!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
            Log.e("TAG","取出缓存的数据..=="+saveJson);
        }
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
                        Log.e("TAG", "请求失败==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "请求成功==" + response);
                        CacheUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                        processData(response);
                    }
                });
    }

    private void processData(String json) {
        //NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class);
        NewsCenterBean newsCenterBean = parseJson(json);

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

    private NewsCenterBean parseJson(String json) {
        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            newsCenterBean.setRetcode(retcode);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            List<NewsCenterBean.DataBean> data = new ArrayList<>();
            newsCenterBean.setData(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                if (jsonObject1 != null) {
                    NewsCenterBean.DataBean dataBean = new NewsCenterBean.DataBean();
                    dataBean.setId(jsonObject1.optInt("id"));
                    dataBean.setType(jsonObject1.optInt("type"));
                    dataBean.setTitle(jsonObject1.optString("title"));
                    dataBean.setUrl(jsonObject1.optString("url"));

                    JSONArray jsonArray1 = jsonObject1.optJSONArray("children");
                    if (jsonArray1 != null){
                        List<NewsCenterBean.DataBean.ChildrenBean> children = new ArrayList<>();
                        dataBean.setChildren(children);
                        for (int j = 0; j < jsonArray1.length(); j++){
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            childrenBean.setId(jsonObject2.optInt("id"));
                            childrenBean.setType(jsonObject2.optInt("type"));
                            childrenBean.setTitle(jsonObject2.optString("title"));
                            childrenBean.setUrl(jsonObject2.optString("url"));

                            children.add(childrenBean);
                        }
                    }

                    data.add(dataBean);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsCenterBean;
    }

    public void swichPager(int prePosition) {
        MenuDetailBasePager basePager = basePagers.get(prePosition);
        View rootView = basePager.rootView;
        fl_content.removeAllViews();
        fl_content.addView(rootView);
        basePager.initData();
    }
}
