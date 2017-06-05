package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2017/6/3.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    private List<NewsCenterBean.DataBean.ChildrenBean> datas;
    private ViewPager vp;
    private List<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    public NewsMenuDetailPager(Context context, List<NewsCenterBean.DataBean.ChildrenBean> children) {
        super(context);
        this.datas = children;

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_news_menu_detail,null);
        vp = (ViewPager) view.findViewById(R.id.vp);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        tabDetailPagers = new ArrayList<>();
        for(int i = 0; i < datas.size(); i++){
            tabDetailPagers.add(new TabDetailPager(context,datas.get(i)));
        }

        vp.setAdapter(new NewsMenuDetailPagerAdapter());
    }

    private class NewsMenuDetailPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0 : tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();
            return rootView;
        }
    }
}
