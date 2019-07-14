package com.kierkasa.taxcount;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.util.AttributeSet;


public class BackImg extends LinearLayout {
    private Scroller scroller;

    public BackImg(Context context) {
        super(context);
    }

    public BackImg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        LayoutInflater.from(context).inflate(R.layout.back_img, this);
    }

    public void smoothScrollTo(int destX, int destY, int duration) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;

        int scrollY = getScrollY();
        int deltaY = destY - scrollY;

        scroller.startScroll(scrollX, scrollY, destX, destY, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
