package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDropDownResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dropname")
    @Expose
    private String dropname;

    public String[] getDropname() {
        return new String[]{dropname};
    }

    public void setDropname(String dropname) {
        this.dropname = dropname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
