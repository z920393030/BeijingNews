package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by My on 2017/6/5.
 */

public class HorizontalScrollViewPager extends ViewPager{
    private float startX;
    private float startY;

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float endY = ev.getY();



                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);

                if (distanceX > distanceY && distanceX > 8) {
                    if((endX - startX > 0) &&getCurrentItem()==0 ){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else if((startX - endX >0) && getCurrentItem()==getAdapter().getCount()-1){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {

                    getParent().requestDisallowInterceptTouchEvent(false);

                }


                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
