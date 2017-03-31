package com.tgf.study.weathersample.net;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by tugaofeng on 17/3/30.
 */
public class HttpUtil {

    public static final String URL_ADDR = "http://guolin.tech/api/china";

    public static final String PIC_ADDR = "http://guolin.tech/api/bing_pic";

    public static void sendRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
