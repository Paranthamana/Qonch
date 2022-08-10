package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncAssetResponse {

    @SerializedName("tagId")
    @Expose
    private String tagId;
    @SerializedName("qrCode")
    @Expose
    private String qrCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("auditTime")
    @Expose
    private String auditTime;
    @SerializedName("Location_id")
    @Expose
    private String location_id;
    @SerializedName("Building_id")
    @Expose
    private String building_id;
    @SerializedName("Floor_id")
    @Expose
    private String floor_id;
    @SerializedName("Section_id")
    @Expose
    private String section_id;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
