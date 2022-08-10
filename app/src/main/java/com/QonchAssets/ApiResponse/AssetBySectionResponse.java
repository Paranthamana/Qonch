package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetBySectionResponse {

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("assets")
    @Expose
    private List<Asset> assets = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }


    public static class Asset {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("tagId")
        @Expose
        private String tagId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("purchaseDate")
        @Expose
        private String purchaseDate;
        @SerializedName("cost")
        @Expose
        private Double cost;
        @SerializedName("purchasedFrom")
        @Expose
        private String purchasedFrom;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("serialNo")
        @Expose
        private String serialNo;
        @SerializedName("model")
        @Expose
        private String model;
        @SerializedName("locationId")
        @Expose
        private Integer locationId;
        @SerializedName("locationName")
        @Expose
        private String locationName;
        @SerializedName("buildingId")
        @Expose
        private Object buildingId;
        @SerializedName("buildingName")
        @Expose
        private Object buildingName;
        @SerializedName("floorId")
        @Expose
        private Object floorId;
        @SerializedName("floorName")
        @Expose
        private Object floorName;
        @SerializedName("sectionId")
        @Expose
        private Integer sectionId;
        @SerializedName("sectionName")
        @Expose
        private String sectionName;
        @SerializedName("departmentId")
        @Expose
        private Object departmentId;
        @SerializedName("departmentName")
        @Expose
        private Object departmentName;
        @SerializedName("displayImageId")
        @Expose
        private Object displayImageId;
        @SerializedName("otherImageId")
        @Expose
        private Object otherImageId;
        @SerializedName("categoryId")
        @Expose
        private Object categoryId;
        @SerializedName("categoryName")
        @Expose
        private Object categoryName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("assignTo")
        @Expose
        private String assignTo;
        @SerializedName("leaseTo")
        @Expose
        private String leaseTo;
        @SerializedName("createdUser")
        @Expose
        private String createdUser;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("modifiedUser")
        @Expose
        private Object modifiedUser;
        @SerializedName("modifiedDate")
        @Expose
        private Object modifiedDate;
        @SerializedName("thumbnail")
        @Expose
        private Object thumbnail;
        @SerializedName("rfId")
        @Expose
        private Object rfId;
        @SerializedName("bleTagId")
        @Expose
        private Object bleTagId;
        @SerializedName("uniqueCode")
        @Expose
        private Object uniqueCode;
        @SerializedName("depreciationRate")
        @Expose
        private Double depreciationRate;
        @SerializedName("additionalValues")
        @Expose
        private Object additionalValues;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public String getPurchasedFrom() {
            return purchasedFrom;
        }

        public void setPurchasedFrom(String purchasedFrom) {
            this.purchasedFrom = purchasedFrom;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public Object getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(Object buildingId) {
            this.buildingId = buildingId;
        }

        public Object getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(Object buildingName) {
            this.buildingName = buildingName;
        }

        public Object getFloorId() {
            return floorId;
        }

        public void setFloorId(Object floorId) {
            this.floorId = floorId;
        }

        public Object getFloorName() {
            return floorName;
        }

        public void setFloorName(Object floorName) {
            this.floorName = floorName;
        }

        public Integer getSectionId() {
            return sectionId;
        }

        public void setSectionId(Integer sectionId) {
            this.sectionId = sectionId;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public Object getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Object departmentId) {
            this.departmentId = departmentId;
        }

        public Object getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(Object departmentName) {
            this.departmentName = departmentName;
        }

        public Object getDisplayImageId() {
            return displayImageId;
        }

        public void setDisplayImageId(Object displayImageId) {
            this.displayImageId = displayImageId;
        }

        public Object getOtherImageId() {
            return otherImageId;
        }

        public void setOtherImageId(Object otherImageId) {
            this.otherImageId = otherImageId;
        }

        public Object getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Object categoryId) {
            this.categoryId = categoryId;
        }

        public Object getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(Object categoryName) {
            this.categoryName = categoryName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAssignTo() {
            return assignTo;
        }

        public void setAssignTo(String assignTo) {
            this.assignTo = assignTo;
        }

        public String getLeaseTo() {
            return leaseTo;
        }

        public void setLeaseTo(String leaseTo) {
            this.leaseTo = leaseTo;
        }

        public String getCreatedUser() {
            return createdUser;
        }

        public void setCreatedUser(String createdUser) {
            this.createdUser = createdUser;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Object getModifiedUser() {
            return modifiedUser;
        }

        public void setModifiedUser(Object modifiedUser) {
            this.modifiedUser = modifiedUser;
        }

        public Object getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Object modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Object getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Object thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Object getRfId() {
            return rfId;
        }

        public void setRfId(Object rfId) {
            this.rfId = rfId;
        }

        public Object getBleTagId() {
            return bleTagId;
        }

        public void setBleTagId(Object bleTagId) {
            this.bleTagId = bleTagId;
        }

        public Object getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(Object uniqueCode) {
            this.uniqueCode = uniqueCode;
        }

        public Double getDepreciationRate() {
            return depreciationRate;
        }

        public void setDepreciationRate(Double depreciationRate) {
            this.depreciationRate = depreciationRate;
        }

        public Object getAdditionalValues() {
            return additionalValues;
        }

        public void setAdditionalValues(Object additionalValues) {
            this.additionalValues = additionalValues;
        }

    }
}
