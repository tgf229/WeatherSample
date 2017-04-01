package com.tgf.study.weathersample.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tgf.study.weathersample.bean.weather.WeatherBean;
import com.tgf.study.weathersample.net.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherService extends Service {
    private static final String TAG = "WeatherService";
    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        requestFromServer();
        loadPicFromServer();
        AlarmManager manger = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this,WeatherService.class);
        PendingIntent pi = PendingIntent.getService(WeatherService.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerTime = SystemClock.elapsedRealtime() + 1000 * 1000 * 1000 * 1;  //1小时更新
        manger.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void requestFromServer(){

        final SharedPreferences sharef = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = sharef.getString("weatherId",null);
        if (!TextUtils.isEmpty(weatherId)){
            String url = "http://guolin.tech/api/weather?key=0550abdf7fc2455ab578e672cb28ee4a&cityid="+weatherId;
            HttpUtil.sendRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "requestFromServer onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String result = response.body().string();
                        Log.i(TAG, "requestFromServer onResponse: "+result);

                        JSONObject obj = new JSONObject(result);
                        JSONArray array = obj.getJSONArray("HeWeather");
                        String bodyStr = array.get(0).toString();

                        Gson gson = new Gson();
                        WeatherBean bean = gson.fromJson(bodyStr, WeatherBean.class);
                        SharedPreferences.Editor editor = sharef.edit();
                        editor.putString("weather_info",bodyStr);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void loadPicFromServer(){
        HttpUtil.sendRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "loadPicFromServer onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "loadPicFromServer onResponse: "+result);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherService.this).edit();
                editor.putString("pic_info",result);
                editor.apply();
            }
        });
    }
}
