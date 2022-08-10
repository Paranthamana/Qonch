package com.QonchAssets.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Adapter.ListOfAssetsAdapter;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.ListofAssetsResponse;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.FragmentListOfAssetsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfAssetsFragment extends Fragment {

    FragmentListOfAssetsBinding listOfAssetsBinding;
    private final List<ListofAssetsResponse.Asset> listOfAssets = new ArrayList<>();

    public ListOfAssetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listOfAssetsBinding = FragmentListOfAssetsBinding.inflate(inflater);
        View view = listOfAssetsBinding.getRoot();

        listOfAssetsApiCall();

        AnimatedRecyclerView recyclerView = new AnimatedRecyclerView.Builder(getContext())
                .orientation(LinearLayoutManager.VERTICAL)
                .layoutManagerType(AnimatedRecyclerView.LayoutManagerType.LINEAR)
                .animation(R.anim.layout_animation_from_bottom)
                .animationDuration(1000)
                .reverse(false)
                .build();
        recyclerView.scheduleLayoutAnimation();

        return view;
    }

    private void listOfAssetsApiCall() {
        System.out.println("url details==>" + Urls.ListOfAssets);
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "" + Urls.ListOfAssets, response -> {
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(MyApplication.context);
            }
            Log.e("onResponse==>", response);
            //JSONObject myResponse;
            try {
               /* myResponse = new JSONObject(response);
                if (myResponse.getString("error").equals("1")) {
                    System.out.println("error");
                } else {*/
                details_json(response);
                // }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
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
                params.put("PageNumber", "1");
                params.put("PageSize", "100");
                System.out.println("params-->" + params);
                return params;
            }
        };
        Volley.newRequestQueue(MyApplication.context).add(request);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void details_json(String json) throws JSONException {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(getActivity());
        }
        JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonArray = jsonObj.getJSONArray("assets");
        Log.d("jsonArray--->", String.valueOf(jsonArray));
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.d("jsonArray length--->", String.valueOf(jsonArray.length()));
            JSONObject obj = jsonArray.getJSONObject(i);
            ListofAssetsResponse.Asset asset = new ListofAssetsResponse.Asset();
            asset.setId(obj.getInt("id"));
            asset.setTagId(obj.getString("tagId"));
            asset.setDescription(obj.getString("description"));
            asset.setPurchaseDate(obj.getString("purchaseDate"));
            asset.setCost(obj.getDouble("cost"));
            asset.setPurchasedFrom(obj.getString("purchasedFrom"));
            asset.setBrand(obj.getString("brand"));
            asset.setSerialNo(obj.getString("serialNo"));
            asset.setModel(obj.getString("model"));
            asset.setLocationId(obj.getInt("locationId"));
            asset.setLocationName(obj.getString("locationName"));
            asset.setBuildingId(obj.getString("buildingId"));
            asset.setBuildingName(obj.getString("buildingName"));
            asset.setFloorId(obj.getString("floorId"));
            asset.setFloorName(obj.getString("floorName"));
            asset.setSectionId(obj.getString("sectionId"));
            asset.setSectionName(obj.getString("sectionName"));
            asset.setDepartmentId(obj.getString("departmentId"));
            asset.setDepartmentName(obj.getString("departmentName"));
            //asset.setDisplayImageId(obj.getInt("displayImageId"));
            asset.setOtherImageId(obj.getString("otherImageId"));
            asset.setCategoryId(obj.getString("categoryId"));
            asset.setCategoryName(obj.getString("categoryName"));
            asset.setStatus(obj.getString("status"));
            asset.setAssignTo(obj.getString("assignTo"));
            asset.setLeaseTo(obj.getString("leaseTo"));
            asset.setCreatedUser(obj.getString("createdUser"));
            asset.setCreatedDate(obj.getString("createdDate"));
            asset.setModifiedUser(obj.getString("modifiedUser"));
            asset.setModifiedDate(obj.getString("modifiedDate"));
            asset.setThumbnail(obj.getString("thumbnail"));
            asset.setRfId(obj.getString("rfId"));
            asset.setBleTagId(obj.getString("bleTagId"));
            asset.setUniqueCode(obj.getString("uniqueCode"));
            asset.setDepreciationRate(obj.getDouble("depreciationRate"));

            listOfAssets.add(asset);

            RecyclerView.Adapter adapter = new ListOfAssetsAdapter( getActivity(),listOfAssets);

            if (listOfAssets.size() == 0){
                listOfAssetsBinding.ivNoData.setVisibility(View.VISIBLE);
            } else {
                listOfAssetsBinding.ivNoData.setVisibility(View.GONE);
            }
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            listOfAssetsBinding.rvListOfAssets.setLayoutManager(llm);
            Collections.sort(listOfAssets, new Comparator<ListofAssetsResponse.Asset>() {
                @Override
                public int compare(ListofAssetsResponse.Asset o1, ListofAssetsResponse.Asset o2) {
                    return o1.getTagId().compareTo(o2.getTagId());
                }
            });

            listOfAssetsBinding.rvListOfAssets.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listOfAssetsBinding.rvListOfAssets.scheduleLayoutAnimation();
            CustomProgressDialog.getInstance().dismiss();

        }


    }


}