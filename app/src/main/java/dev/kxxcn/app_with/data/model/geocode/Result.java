
package dev.kxxcn.app_with.data.model.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("userquery")
    @Expose
    private String userquery;
    @SerializedName("items")
    @Expose
    private List<Item> items;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUserquery() {
        return userquery;
    }

    public void setUserquery(String userquery) {
        this.userquery = userquery;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
