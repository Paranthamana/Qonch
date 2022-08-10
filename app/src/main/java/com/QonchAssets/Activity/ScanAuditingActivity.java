package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Adapter.AuditListAdapter;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.AssetBySectionResponse;
import com.QonchAssets.ApiResponse.ScanAssetsResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.IntentKeys;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.QonchAssets.Model.AssetByFloorModel;
import com.QonchAssets.Model.AuditScreenModel;
import com.QonchAssets.RealmDB.ScanTagIDAndTimeModel;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.ActivityScanAuditingBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScanAuditingActivity extends AppCompatActivity {

    public static String TEMPLATE_NAME = "SignatureAndAddressSeparately";
    ActivityScanAuditingBinding binding;
    TextView txtStatus = null;
    TextView txtTemplate = null;
    TextView tvAssertTagIDView;
    LinearLayout layoutRegions = null;
    private final ArrayList<AuditScreenModel> arrayListData = new ArrayList<>();
    RecyclerView.Adapter<AuditListAdapter.holder> adapter;
    String[] barCodeFormat = new String[]{"EAN", "CODE39", "CODE128", "Pharmacode",
            "Codabar", "ITF-14", "MSI"};
    String QRCodeFormat = "QRCODE";
    private String labelType;
    private String Selected_SectionID;
    private String LocationSelected;
    private String BuildingSelected;
    private String UniqueCodeID;
    private final ArrayList<ScanAssetsResponse> scanAssetsResponseList = new ArrayList<>();
    private final String TAG = "ViewScan";
    private String TagId;
    private final ArrayList<AssetBySectionResponse> assetBySectionResponses = new ArrayList<>();
    private int pendingAssetsValue;
    private final ArrayList<ScanTagIDAndTimeModel> scanTagIDAndTimeList = new ArrayList<>();
    private final long SPLASH_DISPLAY_LENGTH = 2000;
    private String FloorSelected;
    private Bundle bundle1;
    private final ArrayList<String> tagIdValidation = new ArrayList<>();
    private String newSectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanAuditingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle bundle = getIntent().getExtras();
        Selected_SectionID = bundle.getString("Selected_SectionID");
        FloorSelected = bundle.getString("SelectedFloorID");
        BuildingSelected = bundle.getString("SelectedBuildingID");
        LocationSelected = bundle.getString("SelectedLocationID");
        System.out.println("SelectedSection + Floor ID==>" + bundle.toString());
        Constants.floorId = FloorSelected;


        AssetsBySectionApiCall();
        binding.btnViewDetails.setText(getResources().getText(R.string.ViewDetails));
        binding.btnPendingAssets.setText(getResources().getText(R.string.PendingAssets));
        binding.btnTotalAssetsScanned.setText(getResources().getText(R.string.TotalAssetsScanned));
        binding.btnScanTotalAssets.setText(getResources().getText(R.string.TotalAssets));

        txtStatus = findViewById(R.id.txtStatus);
        txtTemplate = findViewById(R.id.txtTemplate);
        layoutRegions = findViewById(R.id.layoutRegeions);
        tvAssertTagIDView = findViewById(R.id.tvAssertTagIDView);

        Realm realm1 = Realm.getDefaultInstance();
        realm1.beginTransaction();
        RealmResults<ScanTagIDAndTimeModel> item = realm1.where(ScanTagIDAndTimeModel.class).findAll();
        if (item != null) {
            item.deleteAllFromRealm();
            // ScanDBModel.deleteAllFromRealm();
        }
        realm1.commitTransaction();

        Realm realm2 = Realm.getDefaultInstance();
        realm2.beginTransaction();
        RealmResults<AssetByFloorModel> byFloorModels = realm2.where(AssetByFloorModel.class).findAll();
        if (byFloorModels != null) {
            byFloorModels.deleteAllFromRealm();
        }
        realm2.commitTransaction();

        registerReceivers();
        binding.ivScanningBack.setOnClickListener(v ->
                        this.finish()
                /*CommonFunctions.getInstance().newIntent(ScanAuditingActivity.this,
                        HomeActivity.class, Bundle.EMPTY, true)*/);

        binding.btnViewDetails.setOnClickListener(v -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(ScanAuditingActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                CustomProgressDialog.getInstance().show(ScanAuditingActivity.this);
                if (arrayListData.size() > 0) {
                    if (pendingAssetsValue <= 0) {
                        new Handler().postDelayed(() -> {
                            bundle1 = new Bundle();
                            bundle1.putString("TotalCount",
                                    String.valueOf(assetBySectionResponses.get(0).getTotalCount()));
                            bundle1.putString("TotalScanned", String.valueOf(arrayListData.size()));
                            bundle1.putString("PendingScanned", String.valueOf(pendingAssetsValue));
                            bundle1.putString("Selected_SectionID", Selected_SectionID);
                            bundle1.putString("Selected_FloorID", FloorSelected);
                            bundle1.putString("Selected_LocationID", LocationSelected);
                            bundle1.putString("Selected_BuildingID", BuildingSelected);

                            CommonFunctions.getInstance().newIntent(ScanAuditingActivity.this,
                                    ScanningAssetsDetailActivity.class, bundle1, false);
                            CustomProgressDialog.getInstance().dismiss();
                            tagIdValidation.clear();
                        }, SPLASH_DISPLAY_LENGTH);
                    } else {
                        CustomProgressDialog.getInstance().dismiss();
                        alertMessagePendingCount();
                    }
                } else {
                    FancyToast.makeText(ScanAuditingActivity.this,
                            Constants.ScanAndProceedViewDetails,
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    CustomProgressDialog.getInstance().dismiss();
                }
            }
        });

        registerReceivers();
    }

    @SuppressLint("SetTextI18n")
    private void alertMessagePendingCount() {
        final Dialog dialog = new Dialog(ScanAuditingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_back);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        Button btnBackCancel = dialog.findViewById(R.id.btnBackCancel);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvBackAlertText = dialog.findViewById(R.id.tvBackAlertText);

        tvBackAlertText.setText(Constants.UnScannedAssets + " " + "\n" + "\n"
                + "(" + pendingAssetsValue + ")" + " " + Constants.totalUnScannedData);

        btnOk.setOnClickListener(v -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(ScanAuditingActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                bundle1 = new Bundle();
                bundle1.putString("TotalCount",
                        String.valueOf(assetBySectionResponses.get(0).getTotalCount()));
                bundle1.putString("TotalScanned", String.valueOf(arrayListData.size()));
                bundle1.putString("PendingScanned", String.valueOf(pendingAssetsValue));
                bundle1.putString("Selected_SectionID", Selected_SectionID);
                bundle1.putString("Selected_FloorID", FloorSelected);
                bundle1.putString("Selected_LocationID", LocationSelected);
                bundle1.putString("Selected_BuildingID", BuildingSelected);
                CommonFunctions.getInstance().newIntent(ScanAuditingActivity.this,
                        ScanningAssetsDetailActivity.class, bundle1, false);
                tagIdValidation.clear();
                dialog.dismiss();
            }
        });

        btnBackCancel.setOnClickListener(v1 -> dialog.dismiss());

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.x = 1;   //x position
        wmlp.y = 100;   //y position
        dialog.show();
    }

    private void AssetsBySectionApiCall() {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.AssetBySection + "sectionId" + "=" + Selected_SectionID + "&" +
                        "PageNumber=1" + "&" + "PageSize=2000",
                response -> {
                    System.out.println("Urls--" + Urls.AssetBySection + "sectionId" + "=" +
                            Selected_SectionID + "&" +
                            "PageNumber=1" + "&" + "PageSize=2000");
                    newSectionId = Selected_SectionID;
                    System.out.println("New id : " + newSectionId);
                    if (response != null) {
                        try {
                            detailJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("Your Array Response", "Data Null");
                    }
                }, error -> Log.e("error is ", "" + error)) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", " Bearer " +
                        SessionManager.getInstance().getAccessToken());
                params.put("floorId", "65");
                params.put("PageNumber", "1");
                params.put("PageSize", "100");
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                System.out.println(params);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MyApplication.context);
        queue.add(request);
    }

    @SuppressLint("SetTextI18n")
    private void detailJson(String json) throws JSONException {

        JSONObject jsonObj = new JSONObject(json);
        AssetBySectionResponse assetBySectionResponse = new AssetBySectionResponse();
        assetBySectionResponse.setTotalCount(jsonObj.getInt("totalCount"));
        assetBySectionResponse.setPageSize(jsonObj.getInt("pageSize"));
        assetBySectionResponses.add(assetBySectionResponse);

        binding.btnScanTotalAssets.setText(getResources().getText(R.string.TotalAssets)
                + " " + assetBySectionResponses.get(0).getTotalCount());

        binding.btnTotalAssetsScanned.setText(getResources().getText(R.string.TotalAssetsScanned)
                + " " + "0");

        pendingAssetsValue = assetBySectionResponses.get(0).getTotalCount() - arrayListData.size();

        binding.btnPendingAssets.setText(getResources().getText(R.string.PendingAssets)
                + " " + "0");

        System.out.println("AssetBySection-->" + jsonObj);
        JSONArray jsonArray = jsonObj.getJSONArray("assets");
        if (jsonArray.length() == 0) {
            binding.tvSelectionView.setText(R.string.DataNotAvailable);
            binding.ivNoDataAvailableScanPage.setVisibility(View.VISIBLE);
            binding.tvNoDataSection.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            tagIdValidation.add(obj.getString("tagId"));
            if (!obj.getString("sectionName").equals("")) {
                binding.tvSelectionView.setText(obj.getString("sectionName"));
            } else {
                System.out.println("Section name not avail in the response");
            }
        }

        System.out.println("tagIdValidationList 1--: " + tagIdValidation);
        System.out.println("SectionId--1" + newSectionId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryProfileList();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceivers();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void queryProfileList() {
        Intent i = new Intent();
        i.setAction(IntentKeys.DATAWEDGE_API_ACTION);
        i.setPackage(IntentKeys.DATAWEDGE_PACKAGE);
        i.putExtra(IntentKeys.EXTRA_GET_PROFILES_LIST, "");
        sendBroadcast(i);
    }

    private void deleteProfile() {
        Intent i = new Intent();
        i.setAction(IntentKeys.DATAWEDGE_API_ACTION);
        i.setPackage(IntentKeys.DATAWEDGE_PACKAGE);
        i.putExtra(IntentKeys.EXTRA_DELETE_PROFILE, IntentKeys.PROFILE_NAME);
        sendBroadcast(i);
    }

    void createProfile() {

        Bundle bMain = new Bundle();

        Bundle bConfigBarcode = new Bundle();
        Bundle bParamsBarcode = new Bundle();
        ArrayList<Bundle> bundlePluginConfig = new ArrayList<>();

        /*###### Configurations for Barcode Input [Start] ######*/
        bConfigBarcode.putString("PLUGIN_NAME", "BARCODE");
        bParamsBarcode.putString("scanner_selection", "auto"); // Make scanner selection as auto
        bParamsBarcode.putString("scanning_mode",
                String.valueOf(IntentKeys.DOCUMENT_CAPTURE_SCANNING_MODE));
        // Set the scanning mode as "Document Capture"
        //bParamsBarcode.putString("illumination_mode", "off");
        // Turn off Illumination to scan a document from a reflective screen
        bParamsBarcode.putString("doc_capture_template", TEMPLATE_NAME); // Give a template name
        bConfigBarcode.putString("RESET_CONFIG", "true"); // Reset existing configurations of barcode input plugin
        bConfigBarcode.putBundle("PARAM_LIST", bParamsBarcode);
        bundlePluginConfig.add(bConfigBarcode);
        /*###### Configurations for Barcode Input [Finish] ######*/

        /*###### Configurations for Intent Output [Start] ######*/
        Bundle bConfigIntent = new Bundle();
        Bundle bParamsIntent = new Bundle();
        bConfigIntent.putString("PLUGIN_NAME", "INTENT");
        bConfigIntent.putString("RESET_CONFIG", "true"); // Reset existing configurations of intent output plugin
        bParamsIntent.putString("intent_output_enabled", "true"); // Enable intent output plugin
        bParamsIntent.putString("intent_action", IntentKeys.INTENT_OUTPUT_ACTION); // Set the intent action
        bParamsIntent.putString("intent_category", "android.intent.category.DEFAULT"); // Set a category for intent
        bParamsIntent.putInt("intent_delivery", 2); // Set intent delivery mechanism, Use "0" for Start Activity, "1" for Start Service, "2" for Broadcast, "3" for start foreground service
        bParamsIntent.putString("intent_use_content_provider", "true"); // Enable content provider
        bConfigIntent.putBundle("PARAM_LIST", bParamsIntent);
        bundlePluginConfig.add(bConfigIntent);
        /*###### Configurations for Intent Output [Finish] ######*/

        //Putting the INTENT and BARCODE plugin settings to the PLUGIN_CONFIG extra
        bMain.putParcelableArrayList("PLUGIN_CONFIG", bundlePluginConfig);


        /*###### Associate this application to the profile [Start] ######*/
        Bundle configApplicationList = new Bundle();
        configApplicationList.putString("PACKAGE_NAME", getPackageName());
        configApplicationList.putStringArray("ACTIVITY_LIST", new String[]{"*"});
        bMain.putParcelableArray("APP_LIST", new Bundle[]{
                configApplicationList
        });
        /* ###### Associate this application to the profile [Finish] ######*/

        bMain.putString("PROFILE_NAME", IntentKeys.PROFILE_NAME); //Specify the profile name
        bMain.putString("PROFILE_ENABLED", "true"); // Enable the profile
        bMain.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");
        bMain.putString("RESET_CONFIG", "true");

        Intent iSetConfig = new Intent();

        iSetConfig.setAction(IntentKeys.DATAWEDGE_API_ACTION);
        iSetConfig.setPackage(IntentKeys.DATAWEDGE_PACKAGE);
        iSetConfig.putExtra("com.symbol.datawedge.api.SET_CONFIG", bMain);
        iSetConfig.putExtra("SEND_RESULT", "COMPLETE_RESULT");
        iSetConfig.putExtra(IntentKeys.COMMAND_IDENTIFIER_EXTRA,
                IntentKeys.COMMAND_IDENTIFIER_CREATE_PROFILE);

        this.sendBroadcast(iSetConfig);
    }

    private void registerReceivers() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentKeys.RESULT_ACTION);
        filter.addAction(IntentKeys.NOTIFICATION_ACTION);
        filter.addAction(IntentKeys.INTENT_OUTPUT_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, filter);
    }

    void unRegisterReceivers() {
        unregisterReceiver(myBroadcastReceiver);
    }

    public void btnOnClickCreateProfile(View view) {
        createProfile();
    }

    public void btnOnClickClearScannedData(View view) {
        layoutRegions.removeAllViews();
    }

    public void btnOnClickScan(View view) {
        if (!CommonFunctions.getInstance().CheckInternetConnection()) {
            FancyToast.makeText(ScanAuditingActivity.this, Constants.CheckInternet,
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        } else {
            Intent i = new Intent();
            i.setPackage(IntentKeys.DATAWEDGE_PACKAGE);
            i.setAction(IntentKeys.DATAWEDGE_API_ACTION);
            i.putExtra(IntentKeys.EXTRA_SOFT_SCAN_TRIGGER, "TOGGLE_SCANNING");
            this.sendBroadcast(i);
        }
    }

    void updateStatus(final String status) {
        runOnUiThread(() -> txtStatus.setText(status));
    }

    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            try {
                String action = intent.getAction();
                Bundle extras = intent.getExtras();

                // Check the profile list returned from the EXTRA_GET_PROFILES_LIST method
                /* ###### Processing quarried profile list [Start] ###### */
                if (intent.hasExtra(IntentKeys.EXTRA_RESULT_GET_PROFILES_LIST)) {

                    String[] profilesList = extras.
                            getStringArray(IntentKeys.EXTRA_RESULT_GET_PROFILES_LIST);
                    List<String> arrProfileList = Arrays.asList(profilesList);
                    // Check if the profile list contains the DocumentCapture profile
                    if (arrProfileList.contains(IntentKeys.PROFILE_NAME)) {
                        System.out.println("Profile Name");
                        //updateStatus("Profile already exists, not creating the profile");
                        // Profile exists, no need to create the profile again
                    } else {
                        // Profile does not exist, create the profile
                        //updateStatus("Profile does not exists. Creating the profile..");
                        createProfile();
                    }

                }
                /* ###### Processing queried profile list [Finish] ###### */

                /* ###### Processing the result of CREATE_PROFILE [Start] ###### */
                else if (extras.containsKey(IntentKeys.COMMAND_IDENTIFIER_EXTRA)) {

                    // Check if the create profile command succeeded for
                    // Barcode Input and Intent Output modules
                    if (extras.getString(IntentKeys.COMMAND_IDENTIFIER_EXTRA)
                            .equalsIgnoreCase(IntentKeys.COMMAND_IDENTIFIER_CREATE_PROFILE)) {
                        ArrayList<Bundle> result_list = (ArrayList<Bundle>) extras.get("RESULT_LIST");
                        if (result_list != null && result_list.size() > 0) {
                            boolean allSuccess = true;
                            String resultInfo = "";
                            // Iterate through the result list for each module
                            for (Bundle result : result_list) {
                                if (result.getString("RESULT")
                                        .equalsIgnoreCase(IntentKeys.INTENT_RESULT_CODE_FAILURE)) {

                                    // Profile creation failed for the module.
                                    // Getting more information on what failed
                                    allSuccess = false;
                                    resultInfo = "Module: " + result
                                            .getString("MODULE") + "\n"; // Name of the module that failed
                                    resultInfo += "Result code: " + result
                                            .getString("RESULT_CODE") + "\n"; // Information on the type of the failure
                                    if (result.containsKey("SUB_RESULT_CODE")) // More Information on the failure if exists
                                        resultInfo += "\tSub Result code: " + result
                                                .getString("SUB_RESULT_CODE") + "\n";

                                    break; // Breaking the loop as there is a failure
                                } else {
                                    // Profile creation success for the module.
                                    resultInfo = "Module: " + result.getString("MODULE") + "\n";
                                    resultInfo += "Result: " + result.getString("RESULT") + "\n";
                                }
                            }

                            if (allSuccess) {
                                updateStatus("Profile created successfully");
                            } else {

                                updateStatus("Profile creation failed!\n\n" + resultInfo);
                                deleteProfile();
                            }
                        }
                    }
                }
                /* ###### Processing the result of CREATE_PROFILE [Finish] ###### */

                /* ###### Processing scanned data from Intent output [Start] ###### */
                else if (action.equals(IntentKeys.INTENT_OUTPUT_ACTION)) {

                    Thread dataProcessingThrad = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            Bundle data = intent.getExtras();
                            if (data != null) {

                                String decodedMode = data.getString(IntentKeys.DECODED_MODE);

                                /* ###### Processing scanned data when ScanningMode is set as "Single" [Start] ###### */
                                if (decodedMode.equals(IntentKeys.SINGLE_DECODE_MODE)) {
                                    processSingleDecode(data);
                                }
                                /* ###### Processing scanned data when ScanningMode is set as "Single" [Finish] ###### */

                                /* ###### Processing scanned data when ScanningMode is set as "SimulScan" [Start] ###### */
                                else if (decodedMode.equals(IntentKeys.MULTIPLE_DECODE_MODE)) {
                                    processMultipleDecode(data);
                                }
                                /* ###### Processing scanned data when ScanningMode is set as "SimulScan" [Finish] ###### */
                            }
                        }
                    });
                    dataProcessingThrad.start();

                }
                /* ###### Processing scanned data from Intent output [Finish] ###### */

            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processSingleDecode(Bundle data) {
        String decodeDataUri = data.getString(IntentKeys.DECODE_DATA_EXTRA);
        String barcodeData = "";
        // Check if the data is coming through the content provider.
        if (decodeDataUri != null) {
            // Data is coming through the content provider, using a Cursor object to extract data
            @SuppressLint("Recycle") Cursor cursor = getContentResolver()
                    .query(Uri.parse(decodeDataUri), null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                @SuppressLint("Range") String labelType = cursor
                        .getString(cursor.getColumnIndex(IntentKeys.LABEL_TYPE));
                @SuppressLint("Range") String dataString = cursor
                        .getString(cursor.getColumnIndex(IntentKeys.STRING_DATA_KEY_SINGLE_BARCODE));

                barcodeData += "\nLabel type: " + labelType;
                barcodeData += "data: " + dataString;
            }
        } else {
            // Data is coming through the Intent bundle itself
            labelType = data.getString(IntentKeys.LABEL_TYPE_TAG);
            String dataString = data.getString(IntentKeys.STRING_DATA_KEY);

            barcodeData += "\nLabel type: " + labelType;
            barcodeData += dataString; //---------"data: " +
        }

        TextView txtBarcodeData = new TextView(getApplicationContext());
        txtBarcodeData.setText(barcodeData);

        showInUI(txtBarcodeData, null);
        updateStatus("Data processing successfully");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"Range", "Recycle"})
    private synchronized void processMultipleDecode(Bundle data) {
        ArrayList<Bundle> fields = data.getParcelableArrayList(IntentKeys.DATA_TAG);
        if (fields == null) // Content provider is not enabled in Intent Output plugin or Scanning mode is not selected as "SimulScan"
        {
            updateStatus("Content provider is not enabled in Intent Output plugin " +
                    "or Scanning mode is not selected as \"SimulScan\".\nPlease check and try again");
            return;
        }
        try {
            // Iterate through each field
            for (Bundle field : fields) {

                String decodeDataUri = field.getString(IntentKeys.FIELD_DATA_URI);
                Cursor cursor = null;
                if (decodeDataUri != null)
                    cursor = getContentResolver().query(Uri.parse(decodeDataUri),
                            null, null, null);
                if (cursor != null) {
                    int imgWidth = 0;
                    int imgHeight = 0;
                    cursor.moveToFirst();

                    String strResultStatusData = "";
                    String labelType = "";

                    try {
                        labelType = cursor.getString(cursor.getColumnIndex(IntentKeys.FIELD_LABEL_TYPE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    strResultStatusData += "\nLabel type: " + labelType;
                    if (labelType.equals(IntentKeys.LABEL_TYPE_SIGNATURE)) {
                        imgWidth = cursor.getInt(cursor.getColumnIndex(IntentKeys.IMAGE_WIDTH_TAG));
                        imgHeight = cursor.getInt(cursor.getColumnIndex(IntentKeys.IMAGE_HEIGHT_TAG));
                        // Checking if signature is present in the field [Start]
                        try {
                            int signature_status = -2;
                            signature_status = cursor.getInt(cursor.getColumnIndex(IntentKeys.COLUMN_SIGNATURE_STATUS));
                            if (signature_status == 1) {
                                //Signature present
                                strResultStatusData += "\nSignature status: Signature is present";
                            } else if (signature_status == 0) {
                                //Signature not present
                                strResultStatusData += "\nSignature status: Signature is not present";
                            } else if (signature_status == -1) {
                                //Signature not requested
                                strResultStatusData += "\nSignature status: Signature check is not requested";
                            } else if (signature_status == -2) {
                                //Signature not requested
                                strResultStatusData += "\nSignature status: Signature check is not supported";
                            }
                        } catch (Exception ex) {
                            strResultStatusData += "\nSignature status: Signature check is not supported";
                        }
                        //Checking if signature is present in the field [Finish]
                        strResultStatusData += "\nImage data: ";
                    } else {
                        String dataString = cursor
                                .getString(cursor.getColumnIndex(IntentKeys.DATA_STRING));
                        strResultStatusData += "\nString data: " + dataString;
                    }

                    String nextURI = cursor.getString(cursor.getColumnIndex(IntentKeys.DATA_NEXT_URI));
                    byte[] binaryData = null;
                    if (nextURI.isEmpty()) { // No data chunks. All data are available in one chunk
                        binaryData = cursor.getBlob(cursor.getColumnIndex(IntentKeys.DECODE_DATA));
                    } else {
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            final String fullDataSize = cursor
                                    .getString(cursor.getColumnIndex(IntentKeys.FULL_DATA_SIZE));
                            int bufferSize = cursor.getInt(cursor
                                    .getColumnIndex(IntentKeys.RAW_DATA_SIZE));
                            baos.write(cursor.getBlob(cursor
                                    .getColumnIndex(IntentKeys.DECODE_DATA))); // Read the first chunk from initial set
                            while (!nextURI.isEmpty()) {
                                Cursor imageDataCursor = getContentResolver()
                                        .query(Uri.parse(nextURI), null,
                                                null, null);
                                if (imageDataCursor != null) {
                                    imageDataCursor.moveToFirst();
                                    bufferSize += imageDataCursor
                                            .getInt(imageDataCursor
                                                    .getColumnIndex(IntentKeys.RAW_DATA_SIZE));
                                    byte[] bufferData = imageDataCursor
                                            .getBlob(imageDataCursor
                                                    .getColumnIndex(IntentKeys.DECODE_DATA));
                                    baos.write(bufferData);
                                    nextURI = imageDataCursor
                                            .getString(imageDataCursor
                                                    .getColumnIndex(IntentKeys.DATA_NEXT_URI));
                                }
                                if (imageDataCursor != null) {
                                    imageDataCursor.close();
                                }

                                updateStatus("Data being processed, please wait..\n" +
                                        bufferSize + "/" + fullDataSize + " bytes merged");
                            }
                            binaryData = baos.toByteArray();
                            baos.close();
                        } catch (final Exception ex) {
                            runOnUiThread(() -> Toast.makeText(ScanAuditingActivity.this, ex.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                        }
                    }

                    final TextView txtBarcodeData = new TextView(getApplicationContext());
                    txtBarcodeData.setText(strResultStatusData);

                    showInUI(txtBarcodeData, null);

                    if (labelType.equals(IntentKeys.LABEL_TYPE_SIGNATURE)) {
                        try {
                            //-- Creating YUV Image and Bitmap Image [Start]
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            YuvImage yuvImage = new YuvImage(binaryData, ImageFormat.NV21,
                                    imgWidth, imgHeight, null);
                            yuvImage.compressToJpeg(new Rect(0, 0, imgWidth, imgHeight),
                                    50, out);
                            byte[] imageBytes = out.toByteArray();


                            Bitmap bmp = null;
                            if (binaryData != null) {
                                bmp = BitmapFactory.decodeByteArray(imageBytes, 0,
                                        imageBytes.length);
                            }
                            final ImageView img = new ImageView(getApplicationContext());
                            img.setImageBitmap(bmp);
                            showInUI(null, img);
                            //-- Creating YUV Image and Bitmap Image [Finish]
                        } catch (final Exception ex) {
                            runOnUiThread(() -> Toast.makeText(ScanAuditingActivity.this,
                                    "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());

                        }
                    }
                }
            }
            updateStatus("Data processing successful");
        } catch (Exception ex) {
            //Log any errors
        }
        System.out.println("SectionId--1" + newSectionId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void showInUI(final TextView textView, final ImageView imageView) {
        runOnUiThread(() -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(ScanAuditingActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                System.out.println(imageView);
                if (SessionManager.getInstance().Update("BarCodeChecked")) {
                    SessionManager.getInstance().Update("QRCode");
                    SessionManager.getInstance().Update("BarCodeChecked");
                } else if (SessionManager.getInstance().Update("QRCode")) {
                    SessionManager.getInstance().Update("BarCodeChecked");
                    SessionManager.getInstance().Update("QRCode");
                }
                UniqueCodeID = "";
                TagId = "";
                if (SessionManager.getInstance().Update("BarCodeChecked")) {
                    assert textView != null;
                    UniqueCodeID = textView.getText().toString().substring(31);
                } else if (SessionManager.getInstance().Update("QRCode")) {
                    assert textView != null;
                    UniqueCodeID = textView.getText().toString().substring(30);
                }
                String splitLabelTYpe = labelType.substring(11);
                System.out.println("labelType:-->" + splitLabelTYpe);
                System.out.println("SectionId--" + Constants.SectionChoose);
                Intent intent = getIntent();

                StringRequest request = new StringRequest(StringRequest.Method.GET,
                        "" + Urls.UniqueCode + "?UniqueCode=" + UniqueCodeID + "&"
                                + "SectionId=" + Constants.SectionChoose,
                        response -> {
                            if (response != null) {
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);
                                    for (int index = 0; index < jsonArray.length(); index++) {
                                        JSONObject obj = jsonArray.getJSONObject(index);
                                        ScanAssetsResponse scanAssetsResponse = new ScanAssetsResponse();
                                        scanAssetsResponse.setId(obj.getInt("id"));
                                        scanAssetsResponse.setTagId(obj.getString("tagId"));
                                        scanAssetsResponse.setUniqueCode(obj.getString("uniqueCode"));

                                        scanAssetsResponseList.add(scanAssetsResponse);
                                        Log.i(TAG, "UniqueCodeApiCall:-" +
                                                scanAssetsResponseList.get(index).getId());
                                        Log.i(TAG, "UniqueCodeApiCall:-" +
                                                scanAssetsResponseList.get(index).getUniqueCode());
                                        Log.i(TAG, "UniqueCodeApiCall:-" +
                                                scanAssetsResponseList.get(index).getTagId());

                                        if (scanAssetsResponse.getUniqueCode().equals(UniqueCodeID)) {
                                            TagId = scanAssetsResponse.getTagId();
                                            System.out.println("AssertTagID-->" + TagId);
                                        } else {
                                            TagId = "Android";
                                        }
                                        System.out.println("AssertTagID-->" + TagId);
                                    }
                                    System.out.println("tagIdValidation------:" + tagIdValidation + " " + TagId);
                                    if (tagIdValidation.size() > 1) {
                                        if (SessionManager.getInstance().Update("BarCodeChecked")) {
                                            if (Arrays.toString(barCodeFormat).contains(splitLabelTYpe)) {
                                                System.out.println("labelType:-->" + splitLabelTYpe);
                                                tvAssertTagIDView.setText(getResources().getText(R.string.BarCodeID));
                                                System.out.println("tagIdValidation---:" + tagIdValidation + " " + TagId);
                                                if (tagIdValidation.contains(TagId)) {
                                                    System.out.println("tagIdValidation-:" + tagIdValidation + " " + TagId);
                                                    Date d = new Date();
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                                                            = new SimpleDateFormat("hh:mm a");
                                                    String currentDateTimeString = sdf.format(d);

                                                    Stream<AuditScreenModel> data = arrayListData.stream().filter(x ->
                                                    {
                                                        assert UniqueCodeID != null;
                                                        return x.getAssertTagId().contentEquals(UniqueCodeID);
                                                    });

                                                    int index = Math.toIntExact(data.count());
                                                    if (index != 1) {
                                                        assert UniqueCodeID != null;
                                                        arrayListData.add(new AuditScreenModel(TagId, UniqueCodeID,
                                                                currentDateTimeString, ""));

                                                    } else {
                                                        FancyToast.makeText(ScanAuditingActivity.this,
                                                                Constants.AlreadyScanned, FancyToast.LENGTH_SHORT,
                                                                FancyToast.ERROR, false).show();
                                                    }

                                                    binding.btnScanTotalAssets.setText(getResources().getText(R.string.TotalAssets)
                                                            + " " + assetBySectionResponses.get(0).getTotalCount());

                                                    binding.btnTotalAssetsScanned.setText(getResources().getText(R.string.TotalAssetsScanned)
                                                            + " " + arrayListData.size());

                                                    pendingAssetsValue = assetBySectionResponses.get(0).getTotalCount() - arrayListData.size();

                                                    binding.btnPendingAssets.setText(getResources().getText(R.string.PendingAssets)
                                                            + " " + pendingAssetsValue);

                                                    ScanTagIDAndTimeModel model = new ScanTagIDAndTimeModel();

                                                    for (int index3 = 0; index3 < arrayListData.size(); index3++) {
                                                        model.setAssertTagID(arrayListData.get(index3).getAssertTagId());
                                                        model.setQrCodeId(arrayListData.get(index3).getAssetQRBarCode());
                                                        model.setTime(arrayListData.get(index3).getAuditTime());

                                                        int finalIndex = index3;
                                                        Stream<ScanTagIDAndTimeModel> data1 = scanTagIDAndTimeList.stream().filter(x ->
                                                        {
                                                            //assert UniqueCodeID != null;
                                                            return x.getAssertTagID().contentEquals(arrayListData.get(finalIndex).getAssertTagId());
                                                        });

                                                        int index4 = Math.toIntExact(data1.count());
                                                        if (index4 != 1) {
                                                            scanTagIDAndTimeList.add(model);
                                                            final ScanTagIDAndTimeModel saveData = scanTagIDAndTimeList.get(index3);

                                                            Realm realm;
                                                            realm = Realm.getDefaultInstance();

                                                            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(saveData));
                                                            System.out.println("scanTagIDAndTimeListSize-->" + scanTagIDAndTimeList.size());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getAssertTagID());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getTime());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getQrCodeId());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData);
                                                        } else {
                                                            System.out.println("scanned not");
                                                        }
                                                    }
                                                } else {
                                                    FancyToast.makeText(ScanAuditingActivity.this,
                                                            Constants.ScannedAssetNotThisSection,
                                                            FancyToast.LENGTH_SHORT,
                                                            FancyToast.ERROR, false).show();
                                                }
                                            } else {
                                                FancyToast.makeText(ScanAuditingActivity.this,
                                                        Constants.UnSupportedFormat, FancyToast.LENGTH_SHORT,
                                                        FancyToast.ERROR, false).show();
                                                System.out.println("labelType:-->:");
                                            }
                                        } else if (SessionManager.getInstance().Update("QRCode")) {

                                            System.out.println("subStringQRCODEID:" + labelType.substring(11));
                                            String splitQRCodeLabelTYpe = labelType.substring(11);
                                            tvAssertTagIDView.setText(getResources().getText(R.string.QRCODE_ID));
                                            if (QRCodeFormat.contains(splitQRCodeLabelTYpe)) {
                                                if (tagIdValidation.contains(TagId)) {
                                                    Date d = new Date();
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                                            new SimpleDateFormat("hh:mm a");
                                                    String currentDateTimeString = sdf.format(d);

                                                    Stream<AuditScreenModel> data = arrayListData.stream().filter(x ->
                                                    {
                                                        assert UniqueCodeID != null;
                                                        return x.getAssertTagId().contentEquals(UniqueCodeID);
                                                    });

                                                    int index = Math.toIntExact(data.count());
                                                    if (index != 1) {
                                                        assert UniqueCodeID != null;
                                                        arrayListData.add(new AuditScreenModel(TagId, UniqueCodeID,
                                                                currentDateTimeString, ""));
                                                    } else {
                                                        FancyToast.makeText(ScanAuditingActivity.this,
                                                                Constants.AlreadyScanned,
                                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                    }
                                                    binding.btnScanTotalAssets.setText(getResources().getText(R.string.TotalAssets)
                                                            + " " + assetBySectionResponses.get(0).getTotalCount());

                                                    binding.btnTotalAssetsScanned.setText(getResources().getText(R.string.TotalAssetsScanned)
                                                            + " " + arrayListData.size());

                                                    pendingAssetsValue = assetBySectionResponses.get(0).getTotalCount() - arrayListData.size();

                                                    binding.btnPendingAssets.setText(getResources().getText(R.string.PendingAssets)
                                                            + " " + pendingAssetsValue);

                                                    ScanTagIDAndTimeModel model = new ScanTagIDAndTimeModel();

                                                    for (int index3 = 0; index3 < arrayListData.size(); index3++) {
                                                        model.setAssertTagID(arrayListData.get(index3).getAssertTagId());
                                                        model.setQrCodeId(arrayListData.get(index3).getAssetQRBarCode());
                                                        model.setTime(arrayListData.get(index3).getAuditTime());

                                                        int finalIndex = index3;
                                                        Stream<ScanTagIDAndTimeModel> data1 = scanTagIDAndTimeList.stream().filter(x ->
                                                        {
                                                            //assert UniqueCodeID != null;
                                                            return x.getAssertTagID().contentEquals(arrayListData.get(finalIndex).getAssertTagId());
                                                        });

                                                        int index4 = Math.toIntExact(data1.count());
                                                        if (index4 != 1) {
                                                            scanTagIDAndTimeList.add(model);
                                                            final ScanTagIDAndTimeModel saveData = scanTagIDAndTimeList.get(index3);
                                                            Realm realm;
                                                            realm = Realm.getDefaultInstance();

                                                            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(saveData));
                                                            System.out.println("scanTagIDAndTimeListSize-->" + scanTagIDAndTimeList.size());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getAssertTagID());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getTime());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData.getQrCodeId());
                                                            System.out.println("scanTagIDAndTimeListSize-->" + saveData);
                                                        } else {
                                                            System.out.println("Already scanned");
                                                        }
                                                    }
                                                } else {
                                                    FancyToast.makeText(ScanAuditingActivity.this,
                                                            Constants.ScannedAssetNotThisSection, FancyToast.LENGTH_SHORT,
                                                            FancyToast.ERROR, false).show();
                                                }

                                            } else {
                                                FancyToast.makeText(ScanAuditingActivity.this, Constants.UnSupportedFormat,
                                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                            }
                                        } else {
                                            FancyToast.makeText(ScanAuditingActivity.this, Constants.SelectAuditConfiguration,
                                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        }
                                    } else {
                                        System.out.println("tagIdValidation is empty");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("response-->", String.valueOf(jsonArray));
                            } else {
                                Log.e("Your Array Response", "Data Null");
                            }
                        }, error -> Log.e("error is ", "" + error)) {

                    //This is for Headers If You Needed
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", " Bearer " + SessionManager.getInstance().getAccessToken());
                        return params;
                    }

                    //Pass Your Parameters here
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        System.out.println(params);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(MyApplication.context);
                queue.add(request);

                adapter = new AuditListAdapter(ScanAuditingActivity.this, arrayListData, textView);
                LinearLayoutManager llm = new LinearLayoutManager(ScanAuditingActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                binding.rvAuditData.setLayoutManager(llm);
                binding.rvAuditData.setHasFixedSize(true);
                binding.rvAuditData.setAdapter(adapter);
            }

        });
    }
}
