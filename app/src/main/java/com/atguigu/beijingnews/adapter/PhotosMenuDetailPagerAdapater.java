package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.atguigu.newsbeijing_library.utils.BitmapCacheUtils;
import com.atguigu.newsbeijing_library.utils.ConstantUtils;
import com.atguigu.newsbeijing_library.utils.NetCachUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by My on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {
    private final Context context;
    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private final RecyclerView recyclerview;

    private BitmapCacheUtils bitmapCacheUtils;
    private DisplayImageOptions options;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCachUtils.SUCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    Log.e("TAG", "请求图片成功==" + position);
                    ImageView imageview = (ImageView) recyclerview.findViewWithTag(position);
                    if (imageview != null && bitmap != null) {
                        imageview.setImageBitmap(bitmap);

                    }


                    break;
                case NetCachUtils.FAIL:
                    position = msg.arg1;
                    Log.e("TAG", "请求图片失败==" + position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas, RecyclerView recyclerview) {
        this.context = context;
        this.datas = datas;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
        this.recyclerview = recyclerview;

         options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.pic_item_list_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.pic_item_list_default)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .displayer(new RoundedBitmapDisplayer(360))//是否设置为圆角，弧度为多少
                //      .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
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
       /* Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl, position);
        holder.ivIcon.setTag(position);
        if(bitmap != null){
            holder.ivIcon.setImageBitmap(bitmap);
        }*/
        ImageLoader.getInstance().displayImage(imageUrl, holder.ivIcon , options);
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
                    String url = ConstantUtils.BASE_URL + datas.get(getLayoutPosition()).getListimage();
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }

}
