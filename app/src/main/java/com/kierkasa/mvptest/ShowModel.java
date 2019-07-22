package com.kierkasa.mvptest;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ShowModel {
    public Call getData(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request);
    }

}
