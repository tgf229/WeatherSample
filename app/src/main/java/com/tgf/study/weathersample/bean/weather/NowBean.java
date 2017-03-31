package com.tgf.study.weathersample.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tugaofeng on 17/3/31.
 */
public class NowBean {

    private String tmp;

    @SerializedName("cond")
    private More more;

    public class More{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
