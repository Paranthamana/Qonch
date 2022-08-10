package com.QonchAssets.Common;

public class Constants {


    private static final Constants commonLanguageInstance = new Constants();

    public static String UserNameEmpty = "";
    public static String PasswordEmpty = "";
    public static String CheckInternet = "";
    public static String pleaseClickBackAgainToExit = "";
    public static String checkUserNameAndPassword = "";
    public static String PendingAssets = "";
    public static String TotalAssets = "";
    public static String TotalAssetsScanned = "";
    public static String ViewDetails = "";
    public static String BarCodeScanningSelected = "";
    public static String ORCodeScanningSelected = "";
    public static String RFIDScanningSelected = "";
    public static String LoginSuccess = "";
    public static String SearchLocation = "";
    public static String AlreadyScanned = "";
    public static String checkUserName = "";
    public static String ChooseYourLocation = "";
    public static String ChooseYourBuilding = "";
    public static String ChooseYourFloor = "";
    public static String ChooseYourSection = "";
    public static String UnSupportedFormat = "";
    public static String SelectAuditConfiguration = "";
    public static String DBName = "QonchAssets";
    public static String floorId= "";
    public static String DataSavedSuccessfully = "";
    public static String UnScannedAssets = "";
    public static String totalUnScannedData = "";
    public static int ScannedTotal = 0;
    public static String userName = "";
    public static String ScannedAssetNotThisSection = "";
    public static String NoDataAvailableSelectOtherLocation = "";
    public static String ScanAndProceedViewDetails = "";
    public static String SectionChoose = "";

    public static Constants getInstance() {
        return commonLanguageInstance;
    }
    private static String Language = "2";

    public void languageConstants() {
        LoginSuccess = ((Language.equals("1")) ? "" : "Login success");
        UserNameEmpty = ((Language.equals("1")) ? "" : "Enter Correct User Name");
        PasswordEmpty = ((Language.equals("1")) ? "" : "Enter Correct Password");
        CheckInternet = ((Language.equals("1")) ? "" : "Check Your internet Connection");
        pleaseClickBackAgainToExit = ((Language.equals("1")) ? "" : "please Click Back Again To Exit");
        checkUserNameAndPassword = ((Language.equals("1")) ? "" : "Check username and password");
        PendingAssets = ((Language.equals("1")) ? "" : "Pending Assets :");
        TotalAssets = ((Language.equals("1")) ? "" : "Total Assets :");
        TotalAssetsScanned = ((Language.equals("1")) ? "" : "Total Assets Scanned :");
        ViewDetails = ((Language.equals("1")) ? "" : "View Details :");
        BarCodeScanningSelected = ((Language.equals("1")) ? "" : "BarCode Scanning Selected");
        ORCodeScanningSelected = ((Language.equals("1")) ? "" : "QR-Code Scanning Selected");
        RFIDScanningSelected = ((Language.equals("1")) ? "" : "RFID Scanning Selected");
        SearchLocation = ((Language.equals("1")) ? "" : "Search Location");
        AlreadyScanned = ((Language.equals("1")) ? "" : "Scan New ID-it's already Scanned");
        checkUserName = ((Language.equals("1")) ? "" : "Username does not exist");
        ChooseYourLocation = ((Language.equals("1")) ? "" : "Choose Your Location");
        ChooseYourBuilding = ((Language.equals("1")) ? "" : "Choose Your Building");
        ChooseYourFloor = ((Language.equals("1")) ? "" : "Choose Your Floor");
        ChooseYourSection = ((Language.equals("1")) ? "" : "Choose Your Section");
        UnSupportedFormat = ((Language.equals("1")) ? "" : "Format Not Valid");
        SelectAuditConfiguration = ((Language.equals("1")) ? "" : "Select audit configuration");
        DataSavedSuccessfully = ((Language.equals("1")) ? "" : "Data Saved Successfully");
        UnScannedAssets = ((Language.equals("1")) ? "" : "Are you sure want View the details..?");
        totalUnScannedData = ((Language.equals("1")) ? "" : "UnScanned Assets are available");
        ScanAndProceedViewDetails = ((Language.equals("1")) ? "" : "Scan and proceed view details");
        ScannedAssetNotThisSection = ((Language.equals("1")) ? "" : "The Scanned Asset don't \n  belong to this section");
        NoDataAvailableSelectOtherLocation = ((Language.equals("1")) ? "" : "      No data available..! \n Please select other location");
    }


    //--- Dummy text ---> home = ((Language.equals("1")) ? "other Language text here" : "Home");
}
