package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LocationApiResponse {

    public LocationApiResponse() {

    }

    @SerializedName("master")
    @Expose
    private List<Master> master = null;

    public List<Master> getMaster() {
        return master;
    }

    public void setMaster(List<Master> master) {
        this.master = master;
    }


    public static class Master {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("longitude")
        @Expose
        private Object longitude;
        @SerializedName("latitude")
        @Expose
        private Object latitude;

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

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

    }

}

