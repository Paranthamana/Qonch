package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingDropDownResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public String[] getDropname() {
        return new String[]{Dropname};
    }

    public void setDropname(String dropname) {
        Dropname = dropname;
    }

    @SerializedName("name")
    @Expose
    private String Dropname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
