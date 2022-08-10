package com.QonchAssets.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.LocationApiResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.ActivityAuditLocationMapBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditLocationMapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    ActivityAuditLocationMapBinding binding;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    List<LocationApiResponse.Master> getLocationApiResponses = new ArrayList<>();
    LocationApiResponse.Master response = new LocationApiResponse.Master();
    GoogleMap map;
    List<Marker> markers1 = new ArrayList<>();
    MarkerOptions markerOptions = new MarkerOptions();
    private final String TAG = "Location";
    private final long DISPLAY_LENGTH = 2000;
    ArrayList<LatLng> MarkerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuditLocationMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (!CustomProgressDialog.getInstance().isShowing()){
            CustomProgressDialog.getInstance().show(AuditLocationMapActivity.this);
        }
        MarkerPoints = new ArrayList<>();

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mMap);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(googleMap -> {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setZoomGesturesEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.animateCamera(CameraUpdateFactory.zoomTo(12));
                //Initialize Google Play Services
                buildGoogleApiClient();
                if (ActivityCompat.checkSelfPermission(AuditLocationMapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MyApplication.context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(false);

                map.setOnMapClickListener(point -> {
                });
            });

            binding.ivLocationBack.setOnClickListener(v -> finish());
        }

        binding.ivLocation.setOnClickListener(v -> {
            try {
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                MarkerOptions markeroptions = new MarkerOptions();
                markeroptions.position(latLng);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(15));
                if (mGoogleApiClient != null) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                            AuditLocationMapActivity.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().show(AuditLocationMapActivity.this);
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(MyApplication.context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        StringRequest request = new StringRequest(Request.Method.GET, Urls.Location, response -> {
            System.out.println("response-Location/List/Map/===>" + Urls.Location);
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

    private void detailJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject obj = jsonArray.getJSONObject(index);
            LocationApiResponse.Master master = new LocationApiResponse.Master();
            master.setId(obj.getInt("id"));
            master.setName(obj.getString("name"));
            master.setLatitude(obj.getString("latitude"));
            master.setLongitude(obj.getString("longitude"));

            getLocationApiResponses.add(master);
            System.out.println("getLocationApiResponses-->" + getLocationApiResponses.get(index).getId());
            System.out.println("getLocationApiResponses-->" + getLocationApiResponses.get(index).getName());
            System.out.println("getLocationApiResponses-->" + getLocationApiResponses.get(index).getLatitude());
            System.out.println("getLocationApiResponses-->" + getLocationApiResponses.get(index).getLongitude());
            System.out.println("Result ==> :" + response.getId() + " " + response.getName() + " "
                    + response.getLatitude() + " " + response.getLongitude());

            try {
                LatLng latLng = new LatLng(Double.parseDouble(obj.getString("latitude")),
                        Double.parseDouble(obj.getString("longitude")));

                Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                        .title(response.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                marker.setTag(index);
                markers1.add(marker);

                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            } catch (Exception e) {
                e.printStackTrace();
            }
            CustomProgressDialog.getInstance().dismiss();
        }

        map.animateCamera(CameraUpdateFactory.zoomTo(11.6F));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }

        map.setOnMarkerClickListener(marker -> {
            Bundle bundle = new Bundle();
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(AuditLocationMapActivity.this);
            }
            new Handler().postDelayed(() -> {

                System.out.println("position--" + marker.getPosition().latitude);
                System.out.println("position--" + marker.getPosition().longitude);
                System.out.println("position--" + marker.getTag());
                System.out.println("position--" + marker.getTitle());

                for (int index2 = 0; index2 < getLocationApiResponses.size(); index2++) {

                    if (String.valueOf(marker.getPosition().latitude)
                            .equals(getLocationApiResponses.get(index2).getLatitude())) {

                        String buildId = getLocationApiResponses.get(index2).getId().toString();
                        System.out.println("buildId--" + buildId);
                        markerOptions.title(getLocationApiResponses.get(index2).getName());

                        bundle.putString("BuildingId", buildId);
                        bundle.putString("BuildingName", getLocationApiResponses.get(index2).getName());
                        bundle.putString("BuildingLat", String.valueOf(marker.getPosition().latitude));
                        bundle.putString("BuildingLang", String.valueOf(marker.getPosition().longitude));
                    } else {
                        System.out.println("buildID---");
                    }


                    Log.i(TAG, "BuildingID:--->" + bundle);
                }
                CommonFunctions.getInstance().newIntent(AuditLocationMapActivity.this,
                        AssetsBuildingActivity.class, bundle, false);
            }, DISPLAY_LENGTH);
            return false;

        });

    }
}