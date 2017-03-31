package com.tgf.study.weathersample.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class BasicBean {

    private String city;

    @SerializedName("id")
    private String weatherId;

    private Update update;

    public class Update{

        @SerializedName("loc")
        private String updateTime;

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
