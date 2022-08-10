package com.QonchAssets.RealmDB;

import java.io.Serializable;

import io.realm.RealmObject;

public class ScanTagIDAndTimeModel extends RealmObject implements Serializable {


    private String AssertTagID;
    private String qrCodeId;
    private String Time;
    private String status;
    private boolean isSynced;
    private String description;

    public ScanTagIDAndTimeModel(){

    }


    public String getAssertTagID() {
        return AssertTagID;
    }

    public void setAssertTagID(String assertTagID) {
        AssertTagID = assertTagID;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
