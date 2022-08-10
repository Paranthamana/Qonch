package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.QonchAssets.Adapter.FloorListOfAssetsAdapter;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.AssetsByFloorResponse;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.qonchAssets.databinding.ActivityAssetsByFloorBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsByFloorActivity extends AppCompatActivity {

    List<AssetsByFloorResponse.Asset> floorList = new ArrayList<>();
    ActivityAssetsByFloorBinding binding;
    FloorListOfAssetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetsByFloorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
        Bundle bundle = getIntent().getExtras();
        String floorId = bundle.getString("position");
        System.out.println("position==>" + bundle.toString());
        byFloorApiCall(floorId);
        binding.ivFloorAssetsBack.setOnClickListener(view1 -> finish());
    }

    private void byFloorApiCall(String floorId) {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.AssetByFloor + "floorId" + "=" + floorId + "&" + "PageNumber=1" + "&" + "PageSize=2000",
                response -> {
                    System.out.println("UrlDetails-->" + Urls.AssetByFloor + "floorId" + "=" + floorId + "&" + "PageNumber=1" + "&" + "PageSize=2000");
            System.out.println("response===>" + response);
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
                params.put("Authorization", " Bearer " + SessionManager.getInstance().getAccessToken());
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
            CustomProgressDialog.getInstance().show(AssetsByFloorActivity.this);
        }
        JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonArray = jsonObj.getJSONArray("assets");
        if (jsonArray.length() == 0){
            binding.ivNoDataFloorList.setVisibility(View.VISIBLE);
            binding.tvNoDataFloorList.setVisibility(View.VISIBLE);
        }
        Log.d("jsonArray--->", String.valueOf(jsonArray));
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.d("jsonArray length--->", String.valueOf(jsonArray.length()));
            JSONObject obj = jsonArray.getJSONObject(i);
            AssetsByFloorResponse.Asset floorResponse = new AssetsByFloorResponse.Asset();
            floorResponse.setTagId(obj.getString("tagId"));
            floorResponse.setDescription(obj.getString("description"));
            floorResponse.setLocationName(obj.getString("locationName"));

            floorList.add(floorResponse);

             adapter = new FloorListOfAssetsAdapter( AssetsByFloorActivity.this,floorList);

            LinearLayoutManager llm = new LinearLayoutManager(AssetsByFloorActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvFloorListOfAssets.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            binding.rvFloorListOfAssets.scheduleLayoutAnimation();
            CustomProgressDialog.getInstance().dismiss();


        }
        CustomProgressDialog.getInstance().dismiss();
    }
}