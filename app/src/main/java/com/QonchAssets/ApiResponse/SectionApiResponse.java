package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionApiResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mapCoords")
    @Expose
    private Object mapCoords;

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

    public Object getMapCoords() {
        return mapCoords;
    }

    public void setMapCoords(Object mapCoords) {
        this.mapCoords = mapCoords;
    }

}
