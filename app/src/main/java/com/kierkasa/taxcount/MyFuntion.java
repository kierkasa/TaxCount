package com.kierkasa.taxcount;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Scroller;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MyFuntion {

    public MyFuntion() {}

    //double类型转String，默认的Double.toString(d)方法会用科学计数法表示double
    public static String douToString(double dou) {
        Double dou_obj = new Double(dou);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String dou_str = nf.format(dou_obj);

        return dou_str;
    }

    //int转String
    public static String intToString(int i) {
        return String.valueOf(i);
    }

    //String转double
    public static double strToDouble(String str) {
        Double dou = Double.parseDouble(str);
        return dou;
    }

    public static int strToInt(String str) {
        int i = Integer.parseInt(str);
        return i;
    }

    public static double toFixed2(double num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        double dou = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return dou;
    }

    public static int toFixed(double num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        int i = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return i;
    }

    public static double getMax(double a, double b, double c, double d, double e, double f, double g ,double h) {
        double max = a>b?a:b;
        max = max>c?max:c;
        max = max>d?max:d;
        max = max>e?max:e;
        max = max>f?max:f;
        max = max>g?max:g;
        max = max>h?max:h;

        return max;
    }

    //创建ColorStateList，第一个为全灰色；第二个为未选中浅绿，选中深绿
    static ColorStateList radio_original = new ColorStateList(
            new int[][]{
                    new int[]{}
            },
            new int[]{
                    Color.rgb(130,130,130),
            }
    );
    static ColorStateList radio_changed = new ColorStateList(
            new int[][]{
                    new int[]{-android.R.attr.state_checked},       //未选中状态
                    new int[]{android.R.attr.state_checked}         //选中状态
            },
            new int[]{
                    Color.rgb(168,216,185),         //与上方选中状态顺序对应
                    Color.rgb(0,170,144),
            }
    );

    //获取手机屏幕宽高
    public static int getScreenSize(Context context, String s) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int w_or_h = 0;
        switch (s) {
            case "width":
                w_or_h = metrics.widthPixels;
                break;
            case "height":
                w_or_h = metrics.heightPixels;
                break;
            default:
                break;
        }
        return w_or_h;
    }

    //dp和px转换
    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale + 0.5f);
    }
    public static int pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale + 0.5f);
    }
}
