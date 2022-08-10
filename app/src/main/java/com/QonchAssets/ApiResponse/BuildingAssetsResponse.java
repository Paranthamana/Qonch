package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuildingAssetsResponse {

    public BuildingAssetsResponse (){

    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("buildingCode")
    @Expose
    private String buildingCode;
    @SerializedName("mapCoords")
    @Expose
    private Object mapCoords;
    @SerializedName("buildingCoordinates")
    @Expose
    private List<BuildingCoordinate> buildingCoordinates = null;

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

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Object getMapCoords() {
        return mapCoords;
    }

    public void setMapCoords(Object mapCoords) {
        this.mapCoords = mapCoords;
    }

    public List<BuildingCoordinate> getBuildingCoordinates() {
        return buildingCoordinates;
    }

    public void setBuildingCoordinates(List<BuildingCoordinate> buildingCoordinates) {
        this.buildingCoordinates = buildingCoordinates;
    }

    public static class BuildingCoordinate {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("buildingId")
        @Expose
        private Integer buildingId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Integer getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(Integer buildingId) {
            this.buildingId = buildingId;
        }

    }
}
