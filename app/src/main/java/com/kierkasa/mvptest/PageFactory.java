package com.kierkasa.mvptest;

import android.graphics.Bitmap;

public abstract class PageFactory {
    public boolean hasData = false;
    public int pageTotal = 0;

    public PageFactory() {}

    public abstract void drawPreviousBitmap(Bitmap bitmap, int pageNum);

    public abstract void drawCurrentBitmap(Bitmap bitmap, int pageNum);

    public abstract void drawNextBitmap(Bitmap bitmap, int pageNum);

    public abstract Bitmap getBitmapByIndex(int index);
}
