package com.QonchAssets.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.QonchAssets.Api.Urls;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.qonchAssets.databinding.ActivityAssetsViewDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AssetsViewDetailsActivity extends AppCompatActivity {

    ActivityAssetsViewDetailsBinding viewDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDetailsBinding = ActivityAssetsViewDetailsBinding.inflate(getLayoutInflater());
        View view = viewDetailsBinding.getRoot();
        setContentView(view);

        Bundle bundle = getIntent().getExtras();
        String assetsId = bundle.getString("AssetsId");

        assetsViewDetailApiCall(assetsId);

        viewDetailsBinding.ivAssetsViewBack.setOnClickListener(v -> finish());

    }

    private void assetsViewDetailApiCall(String assetsId) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(MyApplication.context);
        }
        System.out.println("url details==>" + Urls.ListOfAssets);
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.ListOfAssets + "/" + assetsId, response -> {
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(MyApplication.context);
            }
            Log.e("onResponse==>", response);

            JSONObject myResponse;
            try {
                myResponse = new JSONObject(response);
                JSONObject assetsResponse = myResponse;

                String tagID = assetsResponse.getString("tagId");
                String purchaseDate = assetsResponse.getString("purchaseDate");
                String purchasedFrom = assetsResponse.getString("purchasedFrom");
                String cost = assetsResponse.getString("cost");
                String brand = assetsResponse.getString("brand");
                String model = assetsResponse.getString("model");
                String categoryName = assetsResponse.getString("categoryName");
                String locationName = assetsResponse.getString("locationName");
                String buildingName = assetsResponse.getString("buildingName");
                String floorName = assetsResponse.getString("floorName");
                String sectionName = assetsResponse.getString("sectionName");
                String departmentName = assetsResponse.getString("departmentName");
                String status = assetsResponse.getString("status");

                System.out.println("My Response===> " + myResponse);
                System.out.println("My Response===> " + tagID);


                viewDetailsBinding.tvAssertTagIdValue.setText("  " + tagID);
                viewDetailsBinding.tvPurchaseDateValue.setText("  " + purchaseDate);
                viewDetailsBinding.tvPurchasedFromValue.setText("  " + purchasedFrom);
                viewDetailsBinding.tvCostValue.setText("  " + cost);
                viewDetailsBinding.tvBrandValue.setText("  " + brand);
                viewDetailsBinding.tvModelValue.setText("  " + model);
                viewDetailsBinding.tvCategoryValue.setText("  " + categoryName);
                viewDetailsBinding.tvLocationValue.setText("  " + locationName);
                viewDetailsBinding.tvBuildingValue.setText("  " + buildingName);
                viewDetailsBinding.tvFloorValue.setText("  " + floorName);
                viewDetailsBinding.tvSectionValue.setText("  " + sectionName);
                viewDetailsBinding.tvDepartmentValue.setText("  " + departmentName);
                viewDetailsBinding.tvAssignedToValue.setText("");
                viewDetailsBinding.tvStatusValue.setText("  " + status);
                CustomProgressDialog.getInstance().dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            CustomProgressDialog.getInstance().dismiss();
            if (error.getMessage() != null) {
                Log.d("error", error.getMessage());
            }

        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", " Bearer " + SessionManager.getInstance().getAccessToken());
                return headers;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                System.out.println("params-->" + params);
                return params;
            }
        };
        Volley.newRequestQueue(MyApplication.context).add(request);
    }


}