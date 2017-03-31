package com.tgf.study.weathersample.bean.area;


import com.google.gson.annotations.SerializedName;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class CountyBean extends AreaBean {
    private String cityId;

    @SerializedName("weather_id")
    private String weatherId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
