package com.QonchAssets.Api;

public class Urls {


    /*public static String COMMON = "http://13.233.196.242/assets_api/api/v0/";
    public static String COMMON_1 = "http://13.233.196.242/assets_report_api/api/v0/";*/

    //--- latest url updated on 30.06.2022
    public static String COMMON = "http://103.177.225.48/assets_api/api/v0/";
    public static String COMMON_1 = "http://103.177.225.48/assets_report_api/api/v0/";

    public static final String Login = "Authenticate";
    public static final String Location = COMMON + "Location/List/Map";
    public static final String Building = COMMON + "Building/List/Map/";
    public static final String ListOfAssets = COMMON + "Asset";
    public static final String FloorMap = COMMON + "FloorMap";
    public static final String LocationDropDown = COMMON + "LocationDropDown";
    public static final String BuildingDropDown = COMMON + "BuildingDropDown";
    public static final String FloorDropDown = COMMON + "FloorDropDown";
    public static final String SectionDropDown = COMMON + "SectionDropDown";
    public static final String SectionMap = COMMON + "SectionMap";
    public static final String BuildingImage = COMMON + "BuildingImage";
    public static final String AssetByFloor = COMMON_1 + "AssetByFloor?";
    public static final String ForgotPassword = /*COMMON +*/ "ForgotPassword";
    public static final String UniqueCode = COMMON + "Asset/UniqueCode";
    public static final String AssetBySection = COMMON_1 + "AssetBySection?";
    public static final String pushAssets = "AssetAudit";

}