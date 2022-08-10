package com.QonchAssets.Activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.QonchAssets.Adapter.NavigationMenuAdapter;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.SessionManager;
import com.QonchAssets.Fragment.AuditConfigurationFragment;
import com.QonchAssets.Fragment.AuditLocationFragment;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Fragment.ListOfAssetsFragment;
import com.QonchAssets.Model.NavigationDataModel;
import com.QonchAssets.event.NavigationItemClickEvent;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DISPLAY_LENGTH = 2000;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    DrawerLayout drawerLayout;
    ImageView ivProfilePicture;
    NavigationMenuAdapter navigationMenuAdapter;
    ListView lvNavigationDrawer;
    TextView toolbarTitle;
    TextView tv_username;
    Boolean doubleBackToExitPressedOnce = false;
    private int selectedContent;
    private ImageView ivLocationMap;

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = getApplicationContext();
        Constants.getInstance().languageConstants();
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        EventBus.getDefault().register(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, HomeActivity.this.getTheme());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        } else {
            assert drawable != null;
            drawable.setTint(Color.WHITE);
            toggle.setHomeAsUpIndicator(drawable/*R.drawable.ic_navigation_icon*/);
        }
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);


        toggle.setToolbarNavigationClickListener(v -> {

            View view1 = HomeActivity.this.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        enableExpandableList();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuditLocationFragment()).commit();


    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        lvNavigationDrawer = findViewById(R.id.lvNavigationDrawer);
        toolbarTitle = findViewById(R.id.toolbar_title);
        ivLocationMap = findViewById(R.id.ivLocationMap);
        tv_username = findViewById(R.id.tv_username);

        tv_username.setText(Constants.userName);

        if (SessionManager.getInstance().Update("QRCode") ||
                SessionManager.getInstance().Update("BarCodeChecked")) {
            ivLocationMap.setVisibility(View.VISIBLE);
        } else {
            ivLocationMap.setVisibility(View.GONE);
        }

        ivLocationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                    FancyToast.makeText(HomeActivity.this, Constants.CheckInternet,
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                } else {
                    CommonFunctions.getInstance().newIntent(HomeActivity.this,
                            AuditLocationMapActivity.class, Bundle.EMPTY, false);
                }
            }
        });

        toolbarTitle.setText(Constants.SearchLocation);


    }

    private void enableExpandableList() {
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowProfilePictureDialog();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        //setUserName();
        navigationMenuAdapter = new NavigationMenuAdapter(this, navigationListData());
        lvNavigationDrawer.setAdapter(navigationMenuAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    List<NavigationDataModel> navigationDataModels = new ArrayList<>();

    private List<NavigationDataModel> navigationListData() {
        navigationDataModels.clear();

        NavigationDataModel navigationDataModel = new NavigationDataModel();
        navigationDataModel.setName(Constants.SearchLocation);
        navigationDataModel.setIcons(R.drawable.ic_home_grey);
        navigationDataModel.setId(1);
        navigationDataModels.add(navigationDataModel);

        /*navigationDataModel = new NavigationDataModel();
        navigationDataModel.setName(mContext.getResources().getString(R.string.ListOfAssets));
        navigationDataModel.setIcons(R.drawable.ic_assets);
        navigationDataModel.setId(2);
        navigationDataModels.add(navigationDataModel);*/

        navigationDataModel = new NavigationDataModel();
        navigationDataModel.setName(mContext.getResources().getString(R.string.AuditConfiguration));
        navigationDataModel.setIcons(R.drawable.ic_settings);
        navigationDataModel.setId(2);
        navigationDataModels.add(navigationDataModel);

        navigationDataModel = new NavigationDataModel();
        navigationDataModel.setName(mContext.getResources().getString(R.string.Logout));
        navigationDataModel.setIcons(R.drawable.ic_logout_blue);
        navigationDataModel.setId(3);
        navigationDataModels.add(navigationDataModel);

        return navigationDataModels;
    }

    @Subscribe
    public void onEvent(NavigationItemClickEvent event) {
        selectedContent = event.getSelectedPosition();

        if (selectedContent == 1) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.SearchLocation));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuditLocationFragment()).commit();
            if (SessionManager.getInstance().Update("QRCode") ||
                    SessionManager.getInstance().Update("BarCodeChecked")) {
                ivLocationMap.setVisibility(View.VISIBLE);
            } else {
                ivLocationMap.setVisibility(View.GONE);
            }
        } /*else if (selectedContent == 2) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.ListOfAssets));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListOfAssetsFragment()).commit();
            ivLocationMap.setVisibility(View.GONE);
        }*/ else if (selectedContent == 2) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.AuditConfiguration));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuditConfigurationFragment()).commit();
            ivLocationMap.setVisibility(View.GONE);
        } else if (selectedContent == 3) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.Logout));
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(HomeActivity.this);
            }
            new Handler().postDelayed(() -> {
                SessionManager.getInstance().Logout();
                CommonFunctions.getInstance().newIntent(HomeActivity.this,
                        LoginActivity.class, Bundle.EMPTY, true);
                CustomProgressDialog.getInstance().dismiss();
            }, DISPLAY_LENGTH);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationListData();
        navigationMenuAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (selectedContent == 2) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.SearchLocation));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuditLocationFragment()).commit();
            if (SessionManager.getInstance().Update("QRCode") ||
                    SessionManager.getInstance().Update("BarCodeChecked")) {
                ivLocationMap.setVisibility(View.VISIBLE);
            } else {
                ivLocationMap.setVisibility(View.GONE);
            }
            selectedContent = 0;
        } /*else if (selectedContent == 3) {
            toolbarTitle.setText(mContext.getResources().getString(R.string.SearchLocation));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AuditLocationFragment()).commit();
            ivLocationMap.setVisibility(View.VISIBLE);
            selectedContent = 0;
        }*/ else {
            if (count == 1) {
                super.onBackPressed();
            }
            if (count == 0) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    finishAffinity();
                }
                this.doubleBackToExitPressedOnce = true;
                FancyToast.makeText(HomeActivity.this,
                        Constants.pleaseClickBackAgainToExit,
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        }
    }
}