package com.atguigu.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.pager.BasePager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsPager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.view.NoViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by My on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {

    @InjectView(R.id.vp)
    NoViewPager vp;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new SettingPager(context));

        vp.setAdapter(new MyAdapter());

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        vp.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1,false);
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2,false);
                        break;
                }
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pagers.get(position).initData();

                if(position == 1) {
                    MainActivity main = (MainActivity) context;
                    main.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    MainActivity main = (MainActivity) context;
                    main.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pagers.get(0).initData();

        rgMain.check(R.id.rb_home);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
            basePager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }
}
