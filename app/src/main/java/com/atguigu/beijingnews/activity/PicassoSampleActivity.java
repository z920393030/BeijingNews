package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.atguigu.beijingnews.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class PicassoSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        final PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        String url = getIntent().getData().toString();
        Picasso.with(this)
                .load(url)
                .into(photoView);
    }
}
