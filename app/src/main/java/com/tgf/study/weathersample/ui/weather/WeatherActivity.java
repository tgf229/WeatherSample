package com.tgf.study.weathersample.ui.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tgf.study.weathersample.R;
import com.tgf.study.weathersample.bean.weather.ForecastBean;
import com.tgf.study.weathersample.bean.weather.WeatherBean;
import com.tgf.study.weathersample.net.HttpUtil;
import com.tgf.study.weathersample.service.WeatherService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WeatherActivity";
    private TextView city_name,update_time,now_tmp,now_info,aqi,pm,comfort,car;
    private LinearLayout list_layout;
    private ImageView img_bg,drawer_btn;
    public DrawerLayout drawer_layout;
    private SharedPreferences sharef;
    private  SwipeRefreshLayout swiper_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_btn = (ImageView) findViewById(R.id.drawer_btn);
        drawer_btn.setOnClickListener(this);
        city_name = (TextView) findViewById(R.id.city_name);
        update_time = (TextView) findViewById(R.id.update_time);
        now_tmp = (TextView) findViewById(R.id.now_tmp);
        now_info = (TextView) findViewById(R.id.now_info);
        aqi = (TextView) findViewById(R.id.aqi);
        pm = (TextView) findViewById(R.id.pm);
        comfort = (TextView) findViewById(R.id.comfort);
        car = (TextView) findViewById(R.id.car);
        list_layout = (LinearLayout) findViewById(R.id.list_layout);
        img_bg = (ImageView) findViewById(R.id.img_bg);
        swiper_refresh = (SwipeRefreshLayout) findViewById(R.id.swiper_refresh);
        swiper_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestFromServer(sharef.getString("weatherId","CN101190101"));
            }
        });

        sharef = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String bodyStr = sharef.getString("weather_info",null);
        String picStr = sharef.getString("pic_info",null);
        if (TextUtils.isEmpty(bodyStr)){
            requestFromServer(sharef.getString("weatherId","CN101190101"));
        }else{
            WeatherBean bean = handleResult(bodyStr);
            showView(bean);
        }
        if(TextUtils.isEmpty(picStr)){
            loadPicFromServer();
        }else{
            Glide.with(WeatherActivity.this).load(picStr).into(img_bg);
        }
    }

    private void showView(WeatherBean bean){
        list_layout.removeAllViews();
        for (ForecastBean b : bean.getForecastList()) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.activity_weather_forecast_item, list_layout, false);
            TextView item_time = (TextView) view.findViewById(R.id.item_time);
            TextView item_info = (TextView) view.findViewById(R.id.item_info);
            TextView item_min = (TextView) view.findViewById(R.id.item_min);
            TextView item_max = (TextView) view.findViewById(R.id.item_max);
            item_time.setText(b.getDate());
            item_info.setText(b.getMore().getInfo());
            item_min.setText(b.getTmp().getMin());
            item_max.setText(b.getTmp().getMax());
            list_layout.addView(view);
        }
        city_name.setText(bean.getBasicBean().getCity());
        update_time.setText(bean.getBasicBean().getUpdate().getUpdateTime().substring(11)+"更新");
        now_tmp.setText(bean.getNowBean().getTmp()+" ℃");
        now_info.setText(bean.getNowBean().getMore().getInfo());
        if (bean.getAqiBean() != null){
            aqi.setText(bean.getAqiBean().getCity().getAqi());
            pm.setText(bean.getAqiBean().getCity().getPm25());
        }else{
            aqi.setText("");
            pm.setText("");
        }
        comfort.setText("舒适度: "+bean.getSuggestBean().getComfort().getInfo());
        car.setText("洗车建议: "+bean.getSuggestBean().getCarWash().getInfo());

        Intent intent = new Intent(WeatherActivity.this, WeatherService.class);
        startService(intent);

    }

    private WeatherBean handleResult(String bodyStr){
        Gson gson = new Gson();
        WeatherBean bean = gson.fromJson(bodyStr, WeatherBean.class);

        SharedPreferences.Editor editor = sharef.edit();
        editor.putString("weather_info",bodyStr);
        editor.apply();

        return bean;
    }

    public void requestFromServer(String weatherId){

        SharedPreferences.Editor editor = sharef.edit();
        editor.putString("weatherId",weatherId);
        editor.apply();

        String url = "http://guolin.tech/api/weather?key=0550abdf7fc2455ab578e672cb28ee4a&cityid="+weatherId;
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                        swiper_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Log.i(TAG, "onResponse: "+result);

                    JSONObject obj = new JSONObject(result);
                    JSONArray array = obj.getJSONArray("HeWeather");
                    String bodyStr = array.get(0).toString();
                    final WeatherBean bean = handleResult(bodyStr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showView(bean);
                            swiper_refresh.setRefreshing(false);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadPicFromServer(){
        HttpUtil.sendRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "onResponse: "+result);

                SharedPreferences.Editor editor = sharef.edit();
                editor.putString("pic_info",result);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(result).into(img_bg);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawer_btn:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }
    }
}
