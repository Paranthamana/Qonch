package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.QonchAssets.Adapter.ListOfAssetsSectionAdapter;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.SectionApiResponse;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.qonchAssets.databinding.ActivityAssetBySectionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetBySectionActivity extends AppCompatActivity {

    ActivityAssetBySectionBinding binding;
    private final List<SectionApiResponse> SectionResponse = new ArrayList<>();
    ListOfAssetsSectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetBySectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
        Bundle bundle = getIntent().getExtras();
        String SectionId = bundle.getString("position");
        System.out.println("position==>" + bundle.toString());
        bySectionApiCall(SectionId);

        binding.ivSectionBack.setOnClickListener(v -> finish());
    }

    private void bySectionApiCall(String SectionId) {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.SectionMap + "/" + SectionId,
                response -> {
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
            CustomProgressDialog.getInstance().show(AssetBySectionActivity.this);
        }
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0){
            binding.ivNoDataSectionList.setVisibility(View.VISIBLE);
            binding.tvNoDataSectionList.setVisibility(View.VISIBLE);
        }
        Log.d("jsonArray--->", String.valueOf(jsonArray));
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject obj = jsonArray.getJSONObject(index);
            SectionApiResponse sectionApiResponse = new SectionApiResponse();
            sectionApiResponse.setId(obj.getInt("id"));
            sectionApiResponse.setName(obj.getString("name"));
            sectionApiResponse.setMapCoords(obj.getString("mapCoords"));

            SectionResponse.add(sectionApiResponse);
        }

        adapter = new ListOfAssetsSectionAdapter(AssetBySectionActivity.this, SectionResponse);
        LinearLayoutManager llm = new LinearLayoutManager(AssetBySectionActivity.this);
        binding.rvAssetsSection.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvAssetsSection.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.rvAssetsSection.scheduleLayoutAnimation();
        CustomProgressDialog.getInstance().dismiss();
    }
}