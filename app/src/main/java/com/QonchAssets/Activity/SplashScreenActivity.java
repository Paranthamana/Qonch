package com.QonchAssets.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zebra.qonchAssets.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                STORAGE_PERMISSION_CODE);
    }

    private void checkPermission(String writeExternalStorage, String accessCoarseLocation,
                                 String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this,
                    new String[]{writeExternalStorage, accessCoarseLocation, permission}, requestCode);
        } else {
            Handler handler = new Handler();
            long SPLASH_TIME_OUT = 2000;
            handler.postDelayed(() -> {
                System.out.println("Permission already granted");
                Intent intent =
                        new Intent(SplashScreenActivity.this,
                                LoginActivity.class);
                startActivity(intent);
            }, SPLASH_TIME_OUT);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Storage Permission Granted");
                Intent intent =
                        new Intent(SplashScreenActivity.this,
                                LoginActivity.class);
                startActivity(intent);
            } else {
                System.out.println("Storage Permission Denied");
            }
        }
    }
}