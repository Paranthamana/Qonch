package com.QonchAssets.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetsViewDetailResponse {

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
        private Object sectionId;
        @SerializedName("sectionName")
        @Expose
        private Object sectionName;
        @SerializedName("departmentId")
        @Expose
        private Object departmentId;
        @SerializedName("departmentName")
        @Expose
        private Object departmentName;
        @SerializedName("displayImageId")
        @Expose
        private Integer displayImageId;
        @SerializedName("otherImageId")
        @Expose
        private List<Object> otherImageId = null;
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
        private Object leaseTo;
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
        private String uniqueCode;
        @SerializedName("depreciationRate")
        @Expose
        private Double depreciationRate;
        @SerializedName("additionalValues")
        @Expose
        private List<Object> additionalValues = null;

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

        public Object getSectionId() {
            return sectionId;
        }

        public void setSectionId(Object sectionId) {
            this.sectionId = sectionId;
        }

        public Object getSectionName() {
            return sectionName;
        }

        public void setSectionName(Object sectionName) {
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

        public Integer getDisplayImageId() {
            return displayImageId;
        }

        public void setDisplayImageId(Integer displayImageId) {
            this.displayImageId = displayImageId;
        }

        public List<Object> getOtherImageId() {
            return otherImageId;
        }

        public void setOtherImageId(List<Object> otherImageId) {
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

        public Object getLeaseTo() {
            return leaseTo;
        }

        public void setLeaseTo(Object leaseTo) {
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

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }

        public Double getDepreciationRate() {
            return depreciationRate;
        }

        public void setDepreciationRate(Double depreciationRate) {
            this.depreciationRate = depreciationRate;
        }

        public List<Object> getAdditionalValues() {
            return additionalValues;
        }

        public void setAdditionalValues(List<Object> additionalValues) {
            this.additionalValues = additionalValues;
        }


}
