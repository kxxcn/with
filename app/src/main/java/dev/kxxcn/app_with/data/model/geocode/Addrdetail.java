
package dev.kxxcn.app_with.data.model.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Addrdetail {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sido")
    @Expose
    private String sido;
    @SerializedName("sigugun")
    @Expose
    private String sigugun;
    @SerializedName("dongmyun")
    @Expose
    private String dongmyun;
    @SerializedName("rest")
    @Expose
    private String rest;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSido() {
        return sido;
    }

    public void setSido(String sido) {
        this.sido = sido;
    }

    public String getSigugun() {
        return sigugun;
    }

    public void setSigugun(String sigugun) {
        this.sigugun = sigugun;
    }

    public String getDongmyun() {
        return dongmyun;
    }

    public void setDongmyun(String dongmyun) {
        this.dongmyun = dongmyun;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

}
