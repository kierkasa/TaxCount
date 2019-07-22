package com.kierkasa.mvptest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowPresenter {
    private ShowModel model;
    private MainShow callback;

    public ShowPresenter(MainShow callback) {
        this.callback = callback;
        model = new ShowModel();
    }

    public void getDataTrue(String url) {
        model.getData(url).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.error();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.getMessage(response.body().string());
            }
        });
    }

}
