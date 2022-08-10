package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.BuildingAssetsResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.ActivityMapBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsBuildingActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final long DISPLAY_LENGTH = 2000;

    private GoogleMap mMap;
    private Handler handler;
    ActivityMapBinding binding;
    String Id;
    String Lat;
    String Long;
    List<String> coordinateList = new ArrayList<>();

    private String mapUserId;
    private PolygonOptions poly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.getInstance().languageConstants();
        CustomProgressDialog.getInstance().dismiss();
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(AssetsBuildingActivity.this);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("BuildingId");
        Lat = bundle.getString("BuildingLat");
        Long = bundle.getString("BuildingLang");

        System.out.println("id + lat + long---" + Id + " " + Lat + " " + Long);

        binding.ivBuildingBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            apiStaticDelay();
        }
        assert googleMap != null;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.9601, 78.0766), 8));
    }

    private void apiStaticDelay() {
        if (handler == null) {
            handler = new Handler();
        }
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> {
           /* String response="[{\"id\":60,\"name\":\"annapura towers\",\"buildingCode\":\"0012\",\"mapCoords\":null,\"buildingCoordinates\":[{\"id\":135,\"longitude\":77.5845853266983,\"latitude\":12.924878704905684,\"buildingId\":60},{\"id\":136,\"longitude\":77.58457996228027,\"latitude\":12.924724463930655,\"buildingId\":60},{\"id\":137,\"longitude\":77.58471675494003,\"latitude\":12.924721849676024,\"buildingId\":60},{\"id\":138,\"longitude\":77.58472480156708,\"latitude\":12.924886547664574,\"buildingId\":60}]}]";
            try {
                detailJson(response.substring(96),googleMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            StringRequest request = new StringRequest(Request.Method.GET, Urls.Building + Id, response -> {
                System.out.println("url details==>" + Urls.Building + Id);
                if (response != null) {
                    try {
                        detailJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error is ", "" + error);
                }
            }) {

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
                    params.put("id", Id);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }, 2000);
    }

    @SuppressLint("NewApi")
    private void detailJson(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        //JSONArray jsonArray = jsonObj.getJSONArray("buildingCoordinates");
        System.out.println("BuildingCoOrdinates--" + jsonArray);

        //JSONArray jsonArray = new JSONArray(json);
        LatLng latLngOne = null, latLngTwo = null, latLngThree = null, latLngFour = null;
        JSONObject obj = null;
        ArrayList<LatLng> latLngArrayList = new ArrayList<>();


        for (int index = 0; index < jsonArray.length(); index++) {
            obj = jsonArray.getJSONObject(index);
            BuildingAssetsResponse response = new BuildingAssetsResponse();
            response.setId(obj.getInt("id"));
            response.setName(obj.getString("name"));
            mapUserId = response.getId().toString();

            JSONArray jsonArrays1 = obj.getJSONArray("buildingCoordinates");
            System.out.println("jsonArrays1" + jsonArrays1);
            System.out.println("result" + mapUserId);
            if (jsonArrays1.length() == 0){
                FancyToast.makeText(AssetsBuildingActivity.this, Constants.NoDataAvailableSelectOtherLocation,
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }

            StringBuilder buildingData = new StringBuilder();
            BuildingAssetsResponse.BuildingCoordinate buildingCoordinate =
                    new BuildingAssetsResponse.BuildingCoordinate();
            for (int i = 0; i < jsonArrays1.length(); i++) {
                System.out.println("result" + jsonArrays1.length());

                JSONObject objects1 = jsonArrays1.getJSONObject(i);
                System.out.println("object-->" + objects1.getDouble("latitude"));

                buildingCoordinate.setId(objects1.getInt("id"));
                buildingCoordinate.setBuildingId(objects1.getInt("buildingId"));
                buildingCoordinate.setLatitude(objects1.getDouble("latitude"));
                buildingCoordinate.setLongitude(objects1.getDouble("longitude"));

                latLngOne = new LatLng(objects1.getDouble("latitude"), objects1.getDouble("longitude"));
                latLngArrayList.add(latLngOne);
                System.out.println("lat lang-->" + latLngOne);

                buildingData.append("lat:")
                        .append(objects1.getDouble("latitude"))
                        .append(",")
                        .append("long:")
                        .append(objects1.getDouble("longitude"))
                        .append(",").append("BuildId:")
                        .append(objects1.getInt("buildingId"))
                        .append(",");
                System.out.println("build--con--" + buildingData);
            }
            coordinateList.add(String.valueOf(buildingData));

            if (latLngArrayList.size() > 0) {
                poly = new PolygonOptions();
                for (int i = 0; i < latLngArrayList.size(); i++) {
                    poly.add(latLngArrayList.get(i));
                }
                poly.clickable(true);
                poly.strokeColor(Color.RED);
                poly.fillColor(Color.CYAN);
                mMap.addPolygon(poly);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngArrayList.get(0)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.9F));
                latLngArrayList.clear();
            }
        }
        mMap.setOnPolygonClickListener(polygon -> {
            Bundle bundle = new Bundle();
            String buildingIdSplitValue;
            for (int index7 = 0; index7 < coordinateList.size(); index7++) {
                String strtest = coordinateList.get(index7);
                String[] sArr = strtest.split(",");
                if (strtest.contains(String.valueOf(polygon.getPoints().get(index7).latitude))
                        && strtest.contains(String.valueOf(polygon.getPoints().get(index7).longitude))) {
                    System.out.println("buidid--" + sArr[2]);
                    String[] idSplit = sArr[2].split(":");
                    buildingIdSplitValue = idSplit[1];
                    System.out.println("buildingIdSplitValue--" + buildingIdSplitValue);
                    bundle.putString("idBuildingLatLang", buildingIdSplitValue);
                } else {
                    System.out.println("lat and Long Not matched");
                }
                System.out.println("lat---" + polygon.getPoints().get(index7).latitude);
            }
            CommonFunctions.getInstance().newIntent(AssetsBuildingActivity.this,
                    AssetsInLocationActivity.class, bundle, false);
        });




        /*if (latLngArrayList.size() > 0) {
            PolygonOptions poly = new PolygonOptions();
            for (int i = 0; i < latLngArrayList.size(); i++) {
                poly.add(latLngArrayList.get(i));
            }

            poly.clickable(true);
            poly.strokeColor(Color.RED);
            poly.fillColor(Color.CYAN);
            mMap.addPolygon(poly);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngArrayList.get(0)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(19.5F));


            mMap.setOnPolygonClickListener(polygon -> {
                if (!CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().show(AssetsBuildingActivity.this);
                }
                new Handler().postDelayed(() -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("idBuildingLatLang", mapUserId);
                    CommonFunctions.getInstance().newIntent(AssetsBuildingActivity.this,
                            AssetsInLocationActivity.class, bundle, false);
                }, DISPLAY_LENGTH);
            });

        } else {
            Toast.makeText(getApplicationContext(), "Lat Lang List is empty", Toast.LENGTH_SHORT).show();
        }*/

        new Handler().postDelayed(() -> {
            if (CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().dismiss();
            }
        }, DISPLAY_LENGTH);

    }
}