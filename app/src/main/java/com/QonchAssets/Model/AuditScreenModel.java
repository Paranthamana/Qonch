package com.QonchAssets.Model;

public class AuditScreenModel {

    private String AssetQRBarCode;
    private String AssertTagId;
    private String AuditTime;
    private String Status;

    public AuditScreenModel(String AssetQRBarCode, String AssertTagId, String AuditTime, String Status) {
        this.AssetQRBarCode = AssetQRBarCode;
        this.AssertTagId = AssertTagId;
        this.AuditTime = AuditTime;
        this.Status = Status;
    }

    public String getAssetQRBarCode() {
        return AssetQRBarCode;
    }

    public void setAssetQRBarCode(String assetQRBarCode) {
        AssetQRBarCode = assetQRBarCode;
    }

    public String getAssertTagId() {
        return AssertTagId;
    }

    public void setAssertTagId(String assertTagId) {
        AssertTagId = assertTagId;
    }

    public String getAuditTime() {
        return AuditTime;
    }

    public void setAuditTime(String auditTime) {
        AuditTime = auditTime;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
