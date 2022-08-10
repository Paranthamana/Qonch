package com.QonchAssets.ApiResponse;

public class ScanDataResponse {

    private String AssertTagID;
    private String qrCodeId;
    private String Time;

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
}
