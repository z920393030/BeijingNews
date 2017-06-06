package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.domain.TabDetailPagerBean;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.atguigu.newsbeijing_library.utils.CacheUtils;
import com.atguigu.newsbeijing_library.utils.ConstantUtils;
import com.atguigu.newsbeijing_library.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by My on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {
    public static final String READ_ID_ARRAY = "read_id_array";
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pull_refresh_list;
    ListView lv;
    private String url;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private int prePosition = 0;
    private List<TabDetailPagerBean.DataBean.NewsBean> newsBeanList;
    private ListAdapter adapter;

    private String moreUrl;
    private boolean isLoadingMore = false;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);
        lv = pull_refresh_list.getRefreshableView();
        View view1 = View.inflate(context, R.layout.tab_detail_topnews, null);
        viewpager = (HorizontalScrollViewPager) view1.findViewById(R.id.viewpager);
        tvTitle = (TextView) view1.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) view1.findViewById(R.id.ll_point_group);

        lv.addHeaderView(view1);

        AddSoundListener();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPosition = position - 2;
                TabDetailPagerBean.DataBean.NewsBean newsBean = newsBeanList.get(realPosition);
                String idArray = CacheUtils.getString(context, READ_ID_ARRAY);
                if (!idArray.contains(newsBean.getId() + "")) {
                    idArray = idArray + newsBean.getId() + ",";
                    CacheUtils.putString(context, READ_ID_ARRAY, idArray);
                    adapter.notifyDataSetChanged();
                }
                String url = ConstantUtils.BASE_URL + newsBean.getUrl();
                Intent intent = new Intent(context,NewsDetailActivity.class);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);



            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                llPointGroup.getChildAt(position).setEnabled(true);
                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(topnews.get(position).getTitle());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore = false;
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!TextUtils.isEmpty(moreUrl)) {
                    isLoadingMore = true;
                    getDataFromNet(moreUrl);
                } else {
                    Toast.makeText(context, "没有更多数据了...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void AddSoundListener() {
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pull_refresh_list.setOnPullEventListener(soundListener);
    }

    @Override
    public void initData() {
        super.initData();
        url = ConstantUtils.BASE_URL + childrenBean.getUrl();
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
                        Log.e("TAG", "请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                        pull_refresh_list.onRefreshComplete();
                    }


                });

    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);
        String more = bean.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            moreUrl = ConstantUtils.BASE_URL + more;
        }
        if (!isLoadingMore) {
            topnews = bean.getData().getTopnews();
            viewpager.setAdapter(new MyAdapter());
            tvTitle.setText(topnews.get(prePosition).getTitle());

            llPointGroup.removeAllViews();

            for (int i = 0; i < topnews.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));
                point.setLayoutParams(params);

                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }
                llPointGroup.addView(point);
            }
            newsBeanList = bean.getData().getNews();

            adapter = new ListAdapter();
            lv.setAdapter(adapter);
        } else {
            isLoadingMore = false;
            newsBeanList.addAll(bean.getData().getNews());
            adapter.notifyDataSetChanged();
        }

    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tab_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailPagerBean.DataBean.NewsBean newsBean = newsBeanList.get(position);
            viewHolder.tvDesc.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());
            String imageUrl = ConstantUtils.BASE_URL + newsBean.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);

            String idArray  = CacheUtils.getString(context,READ_ID_ARRAY);
            if(idArray.contains(newsBean.getId()+"")) {
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            }else {
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.iv_icon)
            ImageView ivIcon;
            @InjectView(R.id.tv_desc)
            TextView tvDesc;
            @InjectView(R.id.tv_time)
            TextView tvTime;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.news_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String imageUrl = ConstantUtils.BASE_URL + topnews.get(position).getTopimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}