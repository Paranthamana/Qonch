package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.QonchAssets.Adapter.ListOfAssetsLocationAdapter;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.FloorMapResponse;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zebra.qonchAssets.databinding.ActivityAssetsInLocationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsInLocationActivity extends AppCompatActivity {

    String id;
    ActivityAssetsInLocationBinding binding;
    private final List<FloorMapResponse> floorMapResponse = new ArrayList<>();
    private byte[] BuildImageResponse;
    private String imageUrl;
    ListOfAssetsLocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetsInLocationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("idBuildingLatLang");
        BuildImageApiCall(id);
        System.out.println("idBuildingLatLang == >" + id);
        initView();
    }

    private void BuildImageApiCall(String id) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(AssetsInLocationActivity.this);
        }
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.BuildingImage + "/" + id, response -> {

            imageUrl = Urls.BuildingImage + "/" + id;
            System.out.println("ImageUrl-->" + imageUrl);
            if (response != null) {
                try {
                    BuildingImageJson(response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //binding.ivBuildings.setImageBitmap(StringToBitMap(response));
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

    private void initView() {
        binding.ivLocationBack.setOnClickListener(v -> finish());
    }

    private void floorMapApiCall() {
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.FloorMap + "/" + id, response -> {
            System.out.println("Urls===>" + Urls.FloorMap + "/" + id);
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

    private void BuildingImageJson(String json) throws UnsupportedEncodingException {
        //BuildImageResponse = json;
        String stringToConvert = json;
        byte[] theByteArray = stringToConvert.getBytes();
        BuildImageResponse = theByteArray;
        System.out.println("byteData-->" + Arrays.toString(theByteArray));
        floorMapApiCall();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void detailJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        if (jsonArray.length() == 0){
            binding.ivNoDataLocationList.setVisibility(View.VISIBLE);
            binding.tvNoDataLocationList.setVisibility(View.VISIBLE);
        }

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject obj = jsonArray.getJSONObject(index);
            FloorMapResponse response = new FloorMapResponse();
            response.setId(obj.getInt("id"));
            response.setName(obj.getString("name"));
            floorMapResponse.add(response);

            //Bitmap bm = StringToBitMap(BuildImageResponse);

            adapter = new ListOfAssetsLocationAdapter
                    (AssetsInLocationActivity.this, floorMapResponse, BuildImageResponse);
            LinearLayoutManager llm = new LinearLayoutManager(AssetsInLocationActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rvAssetsLocation.setLayoutManager(llm);

            binding.rvAssetsLocation.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            binding.rvAssetsLocation.scheduleLayoutAnimation();
            CustomProgressDialog.getInstance().dismiss();
        }

        CustomProgressDialog.getInstance().dismiss();
    }

}