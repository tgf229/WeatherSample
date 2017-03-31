package com.tgf.study.weathersample.bean.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class WeatherBean {

    @SerializedName("aqi")
    private AQIBean aqiBean;

    @SerializedName("basic")
    private BasicBean basicBean;

    @SerializedName("now")
    private NowBean nowBean;

    @SerializedName("suggestion")
    private SuggestBean suggestBean;

    @SerializedName("daily_forecast")
    private List<ForecastBean> forecastList;

    public AQIBean getAqiBean() {
        return aqiBean;
    }

    public void setAqiBean(AQIBean aqiBean) {
        this.aqiBean = aqiBean;
    }

    public BasicBean getBasicBean() {
        return basicBean;
    }

    public void setBasicBean(BasicBean basicBean) {
        this.basicBean = basicBean;
    }

    public List<ForecastBean> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<ForecastBean> forecastList) {
        this.forecastList = forecastList;
    }

    public NowBean getNowBean() {
        return nowBean;
    }

    public void setNowBean(NowBean nowBean) {
        this.nowBean = nowBean;
    }

    public SuggestBean getSuggestBean() {
        return suggestBean;
    }

    public void setSuggestBean(SuggestBean suggestBean) {
        this.suggestBean = suggestBean;
    }
}
