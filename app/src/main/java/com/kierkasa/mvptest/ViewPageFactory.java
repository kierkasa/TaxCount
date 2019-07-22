package com.kierkasa.mvptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ViewPageFactory extends PageFactory {
    private Context context;

    private int[] viewIds;

    public ViewPageFactory(Context context, int[] viewIds) {
        this.context = context;
        this.viewIds = viewIds;
        if (viewIds.length > 0) {
            hasData = true;
            pageTotal = viewIds.length;
        }
    }

    @Override
    public void drawPreviousBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum-2),0,0,null);
    }

    @Override
    public void drawCurrentBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum-1),0,0,null);
    }

    @Override
    public void drawNextBitmap(Bitmap bitmap, int pageNum) {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(getBitmapByIndex(pageNum),0,0,null);
    }

    @Override
    public Bitmap getBitmapByIndex(int index) {
        if (hasData) {
            return getBitmapFromIds(index);
        } else {
            return null;
        }
    }

    private Bitmap getBitmapFromIds(int index) {
        return BitmapUtils.drawableToBitmap(
                context.getResources().getDrawable(viewIds[index])
        );
    }

}
