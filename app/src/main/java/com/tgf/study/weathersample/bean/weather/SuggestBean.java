package com.tgf.study.weathersample.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class SuggestBean {

    @SerializedName("comf")
    private Comfort comfort;
    @SerializedName("cw")
    private CarWash carWash;

    public class Comfort{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public class CarWash{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public CarWash getCarWash() {
        return carWash;
    }

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }

    public Comfort getComfort() {
        return comfort;
    }

    public void setComfort(Comfort comfort) {
        this.comfort = comfort;
    }
}
