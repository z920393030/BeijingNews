package com.atguigu.beijingnews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.MainActivity;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.atguigu.beijingnews.pager.NewsPager;

import java.util.List;

/**
 * Created by My on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    List<NewsCenterBean.DataBean> datas;
    private int prePosition = 0;
    private LeftMenuAdapter adapter;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0,40,0,0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prePosition = position;
                adapter.notifyDataSetChanged();
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

                switchPager(prePosition);
            }
        });
        return listView;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;
        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);

        switchPager(prePosition);
    }

    private void switchPager(int postion) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsPager();
        newsPager.swichPager(postion);
    }

    private class LeftMenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return datas==null?0:datas.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            if(prePosition==position){
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }

            textView.setText(datas.get(position).getTitle());
            return textView;
        }
    }
}
