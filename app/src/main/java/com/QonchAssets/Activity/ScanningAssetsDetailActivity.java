package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Adapter.ScanningAssetsViewDetailsAdapter;
import com.QonchAssets.Api.APICommonCallback;
import com.QonchAssets.Api.CommonApiCalls;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.AssetBySectionResponse;
import com.QonchAssets.ApiResponse.ScanDataResponse;
import com.QonchAssets.ApiResponse.SyncAssetResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.QonchAssets.Model.AssetByFloorModel;
import com.QonchAssets.RealmDB.ScanTagIDAndTimeModel;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.ActivityViewScanningAuditBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScanningAssetsDetailActivity extends AppCompatActivity {

    ActivityViewScanningAuditBinding binding;
    RecyclerView.Adapter<ScanningAssetsViewDetailsAdapter.Holder> adapter;
    private RealmResults<ScanTagIDAndTimeModel> ScanDBModel;
    private RealmResults<AssetByFloorModel> assetByFloorModelsDB;
    List<AssetBySectionResponse.Asset> sectionList = new ArrayList<>();
    private String totalCount;
    private final ArrayList<AssetByFloorModel> assets = new ArrayList<>();
    private Realm realmNew;
    private Integer TotalAssetsScanned;
    private String sectionSelected;
    private String buildingSelected;
    private String LocationSelected;
    private String FloorSelected;
    private String TotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewScanningAuditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        realmNew = Realm.getDefaultInstance();
        Bundle bundle = getIntent().getExtras();
        sectionSelected = bundle.getString("Selected_SectionID");
        buildingSelected = bundle.getString("Selected_BuildingID");
        FloorSelected = bundle.getString("Selected_FloorID");
        LocationSelected = bundle.getString("Selected_LocationID");
        TotalCount = bundle.getString("TotalScanned");
        System.out.println("Total count : " + TotalCount);
        AssetsBySectionApiCall(sectionSelected);
        totalCount = bundle.getString("TotalCount");

        initView();
    }


    private void AssetsBySectionApiCall(String SectionSelected) {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.AssetBySection + "sectionId" + "=" + SectionSelected + "&" +
                        "PageNumber=1" + "&" + "PageSize=2000",
                response -> {
                    System.out.println("response===>" + response);
                    System.out.println("Urls===>" + Urls.AssetBySection + "sectionId" + "="
                            + SectionSelected + "&" +
                            "PageNumber=1" + "&" + "PageSize=2000");
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

    @SuppressLint("NotifyDataSetChanged")
    private void detailJson(String json) throws JSONException {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(ScanningAssetsDetailActivity.this);
        }
        JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonArray = jsonObj.getJSONArray("assets");
        Log.d("jsonArray--->", String.valueOf(jsonArray));
        if (jsonArray.length() == 0) {
            binding.tvSelectionAssetsDetailsView.setText(R.string.NoSectionAvailable);
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("jsonArray length--->", String.valueOf(jsonArray.length()));
                JSONObject obj = jsonArray.getJSONObject(i);

                AssetBySectionResponse.Asset floorResponse = new AssetBySectionResponse.Asset();

                AssetByFloorModel assetByFloorModel = new AssetByFloorModel();
                Date d = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                        new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d);
                binding.tvSelectionAssetsDetailsView.setText(obj.getString("sectionName"));

                floorResponse.setTagId(obj.getString("tagId"));
                floorResponse.setId(obj.getInt("id"));
                floorResponse.setDescription(obj.getString("description"));
                floorResponse.setLocationName(obj.getString("locationName"));

                assetByFloorModel.setId(obj.getInt("id"));
                assetByFloorModel.setTagId(obj.getString("tagId"));
                assetByFloorModel.setBuildingName(obj.getString("buildingName"));

                assetByFloorModel.setAuditTime(currentDateTimeString);
                sectionList.add(floorResponse);
                CustomProgressDialog.getInstance().dismiss();

                assets.add(assetByFloorModel);
                System.out.println("assetByFloorModel--" + assetByFloorModel.getQrCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            Realm realmScanData = Realm.getDefaultInstance();
            assetByFloorModelsDB = realmScanData.where(AssetByFloorModel.class).findAll();

            for (int index7 = 0; index7 < sectionList.size(); index7++) {
                if (assetByFloorModelsDB.isEmpty()) {
                    if (sectionList.size() != assetByFloorModelsDB.size()) {
                        for (int index8 = 0; index8 < sectionList.size(); index8++) {
                            final AssetByFloorModel saveData = assets.get(index8);
                            Realm realm;
                            realm = Realm.getDefaultInstance();

                            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(saveData));
                            System.out.println("DB SavedData--" + saveData.getId());
                            System.out.println("DB SavedData--" + saveData.getBuildingName());
                            System.out.println("DB SavedData--" + saveData.getTagId());
                            System.out.println("DB SavedData--" + saveData.getQrCode());
                        }
                    }
                } else {
                    System.out.println("Data avail in local db");
                }
                System.out.println("TagIds1--" + sectionList.get(index7).getTagId());
            }

            for (int j = 0; j < sectionList.size(); j++) {
                System.out.println("TagIds--" + sectionList.get(j).getTagId());
                if (sectionList.get(j).getTagId().equals(assetByFloorModelsDB.get(j).getTagId())) {
                    System.out.println("TagId are equal in the local DB");
                } else {
                    final AssetByFloorModel saveData = assets.get(j);
                    Realm realm;
                    realm = Realm.getDefaultInstance();

                    realm.executeTransaction(realm1 -> realm1.insertOrUpdate(saveData));
                    System.out.println("DB SavedData--" + saveData.getId());
                    System.out.println("DB SavedData--" + saveData.getBuildingName());
                    System.out.println("DB SavedData--" + saveData.getTagId());
                    System.out.println("DB SavedData--" + saveData.getQrCode());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter = new ScanningAssetsViewDetailsAdapter(ScanningAssetsDetailActivity.this,
                ScanDBModel, sectionList, assetByFloorModelsDB);
        LinearLayoutManager llm = new LinearLayoutManager(ScanningAssetsDetailActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvViewAuditData.setLayoutManager(llm);
        binding.rvViewAuditData.setHasFixedSize(true);
        binding.rvViewAuditData.setAdapter(adapter);
        binding.rvViewAuditData.scheduleLayoutAnimation();
        adapter.notifyDataSetChanged();
        CustomProgressDialog.getInstance().dismiss();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void initView() {

        try {
            Realm realmScanData = Realm.getDefaultInstance();
            ScanDBModel = realmScanData.where(ScanTagIDAndTimeModel.class).findAll();

            ScanDataResponse scanDataResponse = new ScanDataResponse();
            ArrayList<ScanDataResponse> dataResponses = new ArrayList<>();
            for (int i = 0; i < ScanDBModel.size(); i++) {
                ScanTagIDAndTimeModel model1 = ScanDBModel.get(i);

                scanDataResponse.setAssertTagID(model1.getAssertTagID());
                scanDataResponse.setTime(model1.getTime());
                scanDataResponse.setQrCodeId(model1.getQrCodeId());
                dataResponses.add(scanDataResponse);

                System.out.println("ScanDBModel-->" + ScanDBModel.get(i).getTime());
                System.out.println("ScanDBModel-->" + ScanDBModel.get(i).getAssertTagID());
                System.out.println("ScanDBModel-->" + ScanDBModel.get(i).getQrCodeId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("size of DB--" + assets.size());
        System.out.println("size of DB--" + ScanDBModel.size());

        //TotalAssetsScanned = ScanDBModel.size();
        TotalAssetsScanned = Integer.valueOf(TotalCount);
        Integer pendingScanned1 = Integer.valueOf(totalCount);
        int countPendingAssets = pendingScanned1 - TotalAssetsScanned;
        System.out.println("count" + countPendingAssets);

        binding.btnViewPendingAssets.setText(getResources().getText(R.string.PendingAssets)
                + " " + countPendingAssets);
        binding.btnViewTotalAssets.setText(getResources().getText(R.string.TotalAssets)
                + " " + totalCount);
        binding.btnViewTotalAssetsScanned.setText(getResources().getText(R.string.TotalAssetsScanned)
                + " " + TotalAssetsScanned);
        binding.btnSaveAssetsDetails.setText(getResources().getText(R.string.Save));

        if (SessionManager.getInstance().Update("BarCodeChecked")) {
            binding.tvViewAssertTagIDView.setText(getResources().getText(R.string.BarCodeID));
        } else {
            binding.tvViewAssertTagIDView.setText(getResources().getText(R.string.QRCODE_ID));
        }

        binding.ivViewAssetsBack.setOnClickListener(v -> backAlertDialog());

        binding.btnSaveAssetsDetails.setOnClickListener(v -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(ScanningAssetsDetailActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                List<ScanTagIDAndTimeModel> list = realmNew.where(ScanTagIDAndTimeModel.class)
                        .equalTo("isSynced", false).findAll();
                try {
                    save(list);
                    ChangeSynced(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void save(List<ScanTagIDAndTimeModel> list) throws IOException, JSONException {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(ScanningAssetsDetailActivity.this);
        }
        String uniqueValue = String.valueOf(new java.sql.Timestamp(System.currentTimeMillis()).getTime());
        File jsonFile = new File(Environment.getExternalStorageDirectory() + "/Download/Assets",
                "save" + uniqueValue + ".json");

        FileWriter writer = new FileWriter(jsonFile, false);
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("tagId", list.get(i).getQrCodeId());
            obj.put("qrCode", list.get(i).getAssertTagID());
            obj.put("status", "Scanned");
            obj.put("auditTime", list.get(i).getTime());
            obj.put("Location_id", LocationSelected);
            obj.put("Building_id", buildingSelected);
            obj.put("Floor_id", FloorSelected);
            obj.put("Section_id", sectionSelected);
            obj.put("description", list.get(i).getDescription());
            jsonArray.put(obj);
            System.out.println("obj--" + obj);
            System.out.println("obj--" + list.get(i).getAssertTagID());
            System.out.println("obj--" + list.get(i).getQrCodeId());
            System.out.println("obj--" + list.get(i).getStatus());
            System.out.println("obj--" + list.get(i).getTime());
        }
        writer.write(String.valueOf(jsonArray));
        writer.close();

        TotalAssetsScanned = 0;
        String InputData = "";

        for (int k = 0; k < list.size(); k++) {
            SyncAssetResponse syncAssetResponse = new SyncAssetResponse();
            syncAssetResponse.setTagId(list.get(k).getQrCodeId());
            syncAssetResponse.setQrCode(list.get(k).getAssertTagID());
            syncAssetResponse.setStatus("Scanned");
            syncAssetResponse.setAuditTime(list.get(k).getTime());
            syncAssetResponse.setBuilding_id(buildingSelected);
            syncAssetResponse.setLocation_id(LocationSelected);
            syncAssetResponse.setFloor_id(FloorSelected);
            syncAssetResponse.setSection_id(sectionSelected);

            Gson gson = new Gson();
            InputData = gson.toJson(syncAssetResponse);
            JSONObject obj1 = new JSONObject(InputData);

            jsonArray1.put(obj1);
        }

        System.out.println("[" + InputData + "]");

        CommonApiCalls.getInstance().SyncAssets(ScanningAssetsDetailActivity.this, String.valueOf(jsonArray1),
                new APICommonCallback.Listener() {
                    @Override
                    public void onSuccess(Object object) {
                        FancyToast.makeText(ScanningAssetsDetailActivity.this,
                                Constants.DataSavedSuccessfully,
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Handler handler = new Handler();
                        long SPLASH_TIME_OUT = 2000;
                        handler.postDelayed(() -> {
                            CommonFunctions.getInstance().newIntent(ScanningAssetsDetailActivity.this,
                                    HomeActivity.class, Bundle.EMPTY, true);
                            CustomProgressDialog.getInstance().dismiss();
                        }, SPLASH_TIME_OUT);
                    }

                    @Override
                    public void onFailure(String reason) {
                        FancyToast.makeText(ScanningAssetsDetailActivity.this,
                                "n",
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    }
                });

        Realm realm1 = Realm.getDefaultInstance();
        realm1.beginTransaction();
        RealmResults<ScanTagIDAndTimeModel> item = realm1.where(ScanTagIDAndTimeModel.class).findAll();
        if (item != null) {
            item.deleteAllFromRealm();
            ScanDBModel.deleteAllFromRealm();
        }
        realm1.commitTransaction();

        Realm realm2 = Realm.getDefaultInstance();
        realm2.beginTransaction();
        RealmResults<AssetByFloorModel> byFloorModels = realm2.where(AssetByFloorModel.class).findAll();
        if (byFloorModels != null) {
            byFloorModels.deleteAllFromRealm();
        }
        realm2.commitTransaction();


        System.out.println("Input Data--" + jsonArray1);


    }

    private void ChangeSynced(List<ScanTagIDAndTimeModel> list) {
        while (list.size() > 0) {
            final ScanTagIDAndTimeModel syncData = list.get(0);
            Realm realm;
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                syncData.setSynced(true);
                realm1.insertOrUpdate(list);
            });
        }
    }

    @Override
    public void onBackPressed() {
        try {
            backAlertDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void backAlertDialog() {
        final Dialog dialog = new Dialog(ScanningAssetsDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_back);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        TextView text = dialog.findViewById(R.id.tvBackAlertText);
        text.setText(R.string.BackAlertMessage);
        Button btnCancel = dialog.findViewById(R.id.btnBackCancel);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        dialog.show();
        btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        btnOk.setOnClickListener(v ->
                        this.finish());
    }

}