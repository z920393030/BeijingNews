package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.atguigu.newsbeijing_library.utils.ConstantUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by My on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {
    private final Context context;
    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;


    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas) {
        this.context = context;
        this.datas = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_photos, null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        holder.tvTitle.setText(newsBean.getTitle());
        String imageUrl = ConstantUtils.BASE_URL + newsBean.getListimage();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    String url = ConstantUtils.BASE_URL+datas.get(getLayoutPosition()).getListimage();
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }
}
