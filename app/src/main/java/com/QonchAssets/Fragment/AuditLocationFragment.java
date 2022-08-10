package com.QonchAssets.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.QonchAssets.Activity.AssetsBuildingActivity;
import com.QonchAssets.Activity.ScanAuditingActivity;
import com.QonchAssets.Api.Urls;
import com.QonchAssets.ApiResponse.BuildingDropDownResponse;
import com.QonchAssets.ApiResponse.FloorDropDownResponse;
import com.QonchAssets.ApiResponse.LocationApiResponse;
import com.QonchAssets.ApiResponse.LocationDropDownResponse;
import com.QonchAssets.ApiResponse.SectionDropDownResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
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
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.FragmentAuditLocationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    FragmentAuditLocationBinding binding;
    private final long DISPLAY_LENGTH = 2000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleMap map;
    ArrayList<LatLng> MarkerPoints;
    LocationApiResponse.Master response = new LocationApiResponse.Master();
    List<LocationApiResponse.Master> getLocationApiResponses = new ArrayList<>();
    MarkerOptions markerOptions = new MarkerOptions();
    String[] BuildingClickSpinner;
    String[] FloorClickSpinner;
    String[] SectionClickSpinner;
    BuildingDropDownResponse dropDownResponse = new BuildingDropDownResponse();
    FloorDropDownResponse floorDropDownResponseModel = new FloorDropDownResponse();
    SectionDropDownResponse sectionDropDownResponseModel = new SectionDropDownResponse();
    private ArrayList<SectionDropDownResponse> SectionList = new ArrayList<>();
    private ArrayList<FloorDropDownResponse> FloorList = new ArrayList<>();
    private final ArrayList<BuildingDropDownResponse> BuildingList = new ArrayList<>();
    private final ArrayList<LocationDropDownResponse> LocationList = new ArrayList<>();
    ArrayList<String> apiNameData = new ArrayList<>();
    ArrayList<String> apiBuildingData = new ArrayList<>();
    ArrayList<String> apiFloorData = new ArrayList<>();
    ArrayList<String> apiSectionData = new ArrayList<>();
    private final String TAG = "LocationSpinner";
    private int SelectedFloor_Spinner;
    private int SelectedLocation_Spinner;
    private int SelectedBuilding_Spinner;
    private int SelectedSections_Spinner;
    private String sectionId = "";
    private String FloorId;
    private String BuildingID;
    private String LocationID;
    List<Marker> markers1 = new ArrayList<>();
    private String str = "";

    public AuditLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAuditLocationBinding.inflate(inflater);
        Constants.getInstance().languageConstants();
        View view = binding.getRoot();
        MarkerPoints = new ArrayList<>();
        if (!CommonFunctions.getInstance().CheckInternetConnection()) {
            FancyToast.makeText(getActivity(), Constants.CheckInternet,
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        } else {
            System.out.println("Connected");
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mMap);
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
                if (ActivityCompat.checkSelfPermission(requireActivity(),
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
        }

        System.out.println("SelectedFloor_Spinner-->" + SelectedFloor_Spinner);

        binding.btnSearch.setOnClickListener(v -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(getActivity(), Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("Selected_SectionID", sectionId);
                bundle.putString("SelectedFloorID", FloorId);
                bundle.putString("SelectedBuildingID", BuildingID);
                bundle.putString("SelectedLocationID", LocationID);
                if (!CustomProgressDialog.getInstance().isShowing()) {
                    CustomProgressDialog.getInstance().show(getActivity());
                }
                new Handler().postDelayed(() -> {
                    if (SelectedLocation_Spinner == 0) {
                        FancyToast.makeText(getActivity(), Constants.ChooseYourLocation,
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        CustomProgressDialog.getInstance().dismiss();
                    } else if (SelectedBuilding_Spinner == 0) {
                        FancyToast.makeText(getActivity(), Constants.ChooseYourBuilding,
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        CustomProgressDialog.getInstance().dismiss();
                    } else if (SelectedFloor_Spinner == 0) {
                        FancyToast.makeText(getActivity(), Constants.ChooseYourFloor,
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        CustomProgressDialog.getInstance().dismiss();
                    } else if (SelectedSections_Spinner == 0) {
                        FancyToast.makeText(getActivity(), Constants.ChooseYourSection,
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        CustomProgressDialog.getInstance().dismiss();
                    } else {
                        if (SessionManager.getInstance().Update("QRCode") ||
                                SessionManager.getInstance().Update("BarCodeChecked")) {
                            CommonFunctions.getInstance().newIntent(getActivity(),
                                    ScanAuditingActivity.class, bundle, false);
                            CustomProgressDialog.getInstance().dismiss();
                        } else {
                            CustomProgressDialog.getInstance().dismiss();
                            FancyToast.makeText(getActivity(), Constants.SelectAuditConfiguration,
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                }, DISPLAY_LENGTH);
            }
        });

        binding.ivLocation.setOnClickListener(v -> {
            try {
                if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                    FancyToast.makeText(getActivity(), Constants.CheckInternet,
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                } else {
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    MarkerOptions markeroptions = new MarkerOptions();
                    markeroptions.position(latLng);
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15));
                    if (mGoogleApiClient != null) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                                (LocationListener) getActivity());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        LocationDropDownApi();
        return view;
    }


    private void LocationDropDownApi() {
        StringRequest request = new StringRequest(Request.Method.GET, Urls.LocationDropDown, response -> {
            System.out.println("response===>" + response);
            if (response != null) {
                try {
                    detailLocationJson(response);
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

    private void BuildingClickApi() {
        StringRequest requestBuilding = new StringRequest(Request.Method.GET,
                Urls.BuildingDropDown + "/" + LocationID, response -> {
            System.out.println("response===>" + response);
            if (response != null) {
                try {
                    detailBuildingJson(response);
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
        RequestQueue queues = Volley.newRequestQueue(MyApplication.context);
        queues.add(requestBuilding);
    }

    private void FloorClickApi() {
        StringRequest requestBuilding = new StringRequest(Request.Method.GET,
                Urls.FloorDropDown + "/" + BuildingID, response -> {
            System.out.println("response===>" + response);
            if (response != null) {
                try {
                    detailFloorJson(response);
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
        RequestQueue queues = Volley.newRequestQueue(MyApplication.context);
        queues.add(requestBuilding);
    }

    private void SectionClickApi() {
        StringRequest requestBuilding = new StringRequest(Request.Method.GET,
                Urls.SectionDropDown + "/" + FloorId, response -> {
            System.out.println("response===>" + response);
            if (response != null) {
                try {
                    detailSectionJson(response);
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
        RequestQueue queues = Volley.newRequestQueue(MyApplication.context);
        queues.add(requestBuilding);
    }

    private void detailLocationJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println("responseBuildName==>" + jsonArray);
        if (apiNameData != null) {
            apiNameData.clear();
        } else {
            apiNameData = new ArrayList<>();
            apiFloorData.add(0, "---Location---");
        }

        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject obj1 = jsonArray.getJSONObject(index);

            LocationDropDownResponse dropDownResponse = new LocationDropDownResponse();
            dropDownResponse.setId(obj1.getInt("id"));
            dropDownResponse.setName(obj1.getString("name"));
            dropDownResponse.setDropname(obj1.getString("name"));


            apiNameData.add(dropDownResponse.getName());
            LocationList.add(dropDownResponse);
            CustomProgressDialog.getInstance().dismiss();

        }
        apiNameData.add(0, "--- Location ---");
        LocationList.add(0, LocationList.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MyApplication.context,
                R.layout.log_spinner_item, apiNameData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.LocationSpinner.setAdapter(adapter);

        binding.LocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("--- Location ---")) {
                    //onNothingSelected(parent);
                    Log.d(TAG, "LocationSelected:--" + position);
                    SelectedLocation_Spinner = binding.LocationSpinner.getSelectedItemPosition();
                } else {
                    Log.d(TAG, "LocationFloorSelected :" + binding.LocationSpinner.getSelectedItemPosition());
                    SelectedLocation_Spinner = binding.LocationSpinner.getSelectedItemPosition();
                    System.out.println("SelectedLocationID-->" + SelectedLocation_Spinner);
                    LocationID = String.valueOf(LocationList.get(position).getId());
                }

                if (SelectedLocation_Spinner > 0) {
                    BuildingClickApi();
                } else {
                    System.out.println("No api call");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
    }

    private void detailBuildingJson(String json) throws JSONException {
        System.out.println("responseBuildName2-->" + json);
        JSONArray jsonArray = new JSONArray(json);
        try {
            if (apiBuildingData != null) {
                apiBuildingData.clear();
            } else {
                apiBuildingData = new ArrayList<>();
            }

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject obj2 = jsonArray.getJSONObject(index);
                System.out.println("responseBuildName2-->" + obj2);
                BuildingDropDownResponse buildingDropDownResponse = new BuildingDropDownResponse();
                buildingDropDownResponse.setId(obj2.getInt("id"));
                buildingDropDownResponse.setName(obj2.getString("name"));
                buildingDropDownResponse.setDropname(obj2.getString("name"));
                BuildingClickSpinner = buildingDropDownResponse.getDropname();


                dropDownResponse.setId(obj2.getInt("id"));

                apiBuildingData.add(buildingDropDownResponse.getName());
                BuildingList.add(buildingDropDownResponse);

            }
            apiBuildingData.add(0, "--- Building ---");
            BuildingList.add(0, BuildingList.get(0));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.log_spinner_item, apiBuildingData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spBuildingSpinner.setAdapter(adapter);

            binding.spBuildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).equals("--- Building ---")) {
                        onNothingSelected(parent);
                        SelectedBuilding_Spinner = binding.spBuildingSpinner.getSelectedItemPosition();
                    } else {
                        Log.d(TAG, "LocationBuildingSelected :" + binding.spBuildingSpinner.getSelectedItemPosition());
                        SelectedBuilding_Spinner = binding.spBuildingSpinner.getSelectedItemPosition();
                        System.out.println("SelectedBuildingID-->" + SelectedBuilding_Spinner);
                        BuildingID = String.valueOf(BuildingList.get(position).getId());
                    }

                    if (SelectedBuilding_Spinner > 0) {
                        FloorClickApi();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void detailFloorJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println("responseFloor==>" + jsonArray);
        try {

            if (apiFloorData != null) {
                apiFloorData.clear();
            } else {
                apiFloorData = new ArrayList<>();
            }

            if (FloorList != null) {
                FloorList.clear();
            } else {
                FloorList = new ArrayList<>();
            }

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject obj3 = jsonArray.getJSONObject(index);
                FloorDropDownResponse floorDropDownResponse = new FloorDropDownResponse();
                floorDropDownResponse.setId(obj3.getInt("id"));
                floorDropDownResponse.setName(obj3.getString("name"));
                floorDropDownResponse.setDropName(obj3.getString("name"));
                FloorClickSpinner = floorDropDownResponse.getDropName();

                floorDropDownResponseModel.setId(obj3.getInt("id"));
                FloorList.add(floorDropDownResponse);
                System.out.println("Floor Names:-->" + Arrays.toString(floorDropDownResponse.getDropName()));


                apiFloorData.add(floorDropDownResponse.getName());
            }

            apiFloorData.add(0, "--- Floor ---");
            FloorList.add(0, FloorList.get(0));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.log_spinner_item, apiFloorData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.FloorSpinner.setAdapter(adapter);

            binding.rlLocation.setOnClickListener(v -> {
                if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                    FancyToast.makeText(getActivity(), Constants.CheckInternet,
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                } else {
                    System.out.println("Connected");
                }
            });

            binding.FloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).equals("--- Floor ---")) {
                        onNothingSelected(parent);
                        SelectedFloor_Spinner = binding.FloorSpinner.getSelectedItemPosition();
                        System.out.println("FloorPosition :" + floorDropDownResponseModel.getId());
                    } else {
                        Log.d(TAG, "LocationFloorSelected :" + binding.FloorSpinner.getSelectedItemPosition());
                        SelectedFloor_Spinner = binding.FloorSpinner.getSelectedItemPosition();
                        System.out.println("SelectedID-->" + SelectedFloor_Spinner);

                        System.out.println("FloorPosition1 :" + floorDropDownResponseModel.getId());
                        FloorId = String.valueOf(FloorList.get(position).getId());
                        // Toast.makeText(getActivity(), FloorId, Toast.LENGTH_SHORT).show();
                    }

                    if (SelectedFloor_Spinner > 0) {
                        SectionClickApi();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void detailSectionJson(String json) throws JSONException {
        try {
            JSONArray jsonArray;
            jsonArray = new JSONArray(json);
            if (apiSectionData != null) {
                apiSectionData.clear();
            } else {
                apiSectionData = new ArrayList<>();
            }
            if (SectionList != null) {
                SectionList.clear();
            } else {
                SectionList = new ArrayList<>();
            }
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject obj4 = jsonArray.getJSONObject(index);
                SectionDropDownResponse sectionDropDownResponse = new SectionDropDownResponse();
                sectionDropDownResponse.setId(obj4.getInt("id"));
                sectionDropDownResponse.setName(obj4.getString("name"));
                sectionDropDownResponse.setDropName(obj4.getString("name"));
                SectionClickSpinner = sectionDropDownResponse.getDropName();
                SectionList.add(sectionDropDownResponse);
                sectionDropDownResponseModel.setId(obj4.getInt("id"));
                apiSectionData.add(sectionDropDownResponse.getName());
            }

            apiSectionData.add(0, "--- Section ---");
            SectionList.add(0, SectionList.get(0));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.log_spinner_item, apiSectionData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.SectionSpinner.setAdapter(adapter);

            binding.SectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).equals("--- Section ---")) {
                        onNothingSelected(parent);
                        SelectedSections_Spinner = binding.SectionSpinner.getSelectedItemPosition();
                    } else {
                        Log.d(TAG, "LocationFloorSelected :" + binding.SectionSpinner.getSelectedItemPosition());
                        SelectedSections_Spinner = binding.SectionSpinner.getSelectedItemPosition();
                        Constants.SectionChoose = "";
                        System.out.println("SectionChoose--" + Constants.SectionChoose);
                        sectionId = String.valueOf(SectionList.get(position).getId());
                        Constants.SectionChoose = String.valueOf(SectionList.get(position).getId());
                        System.out.println("SectionChoose--" + Constants.SectionChoose);
                        // Toast.makeText(getActivity(), sectionId, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            CustomProgressDialog.getInstance().show(getActivity());
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
        CustomProgressDialog.getInstance().dismiss();
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
        }

        map.animateCamera(CameraUpdateFactory.zoomTo(11.6F));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }


        map.setOnMarkerClickListener(marker -> {
            Bundle bundle = new Bundle();
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(getActivity());
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
                CommonFunctions.getInstance().newIntent(getActivity(),
                        AssetsBuildingActivity.class, bundle, false);
                CustomProgressDialog.getInstance().dismiss();
            }, DISPLAY_LENGTH);
            return false;

        });

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
    }

}