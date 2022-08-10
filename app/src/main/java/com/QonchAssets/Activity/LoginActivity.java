package com.QonchAssets.Activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.QonchAssets.Api.APICommonCallback;
import com.QonchAssets.Api.CommonApiCalls;
import com.QonchAssets.ApiResponse.ForgotPasswordApiResponse;
import com.QonchAssets.ApiResponse.LoginApiResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.CustomProgressDialog;
import com.QonchAssets.Common.MyApplication;
import com.QonchAssets.Common.SessionManager;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    Boolean doubleBackToExitPressedOnce = false;
    private String user;
    private String pass;
    boolean isRemember = false;
    Vibrator vibrator;
    Animation shake;
    private static final long DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        Constants.getInstance().languageConstants();
        setContentView(view);
        if (MyApplication.context == null) {
            MyApplication.context = getApplicationContext();
        }
        initView();
    }

    private void initView() {
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (CommonFunctions.getInstance().CheckInternetConnection()) {
            System.out.println("NetWork Available");
        } else {
            FancyToast.makeText(LoginActivity.this, "Check your internet Connection",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        }

        loginBinding.edtUserName.setText(user);
        loginBinding.edtPassword.setText(pass);

        user = SessionManager.getData(SessionManager.USERNAME, "");
        pass = SessionManager.getData(SessionManager.PASSWORD, "");

        if (!user.equals("") && !pass.equals("")) {
            loginBinding.edtUserName.setText(user);
            loginBinding.edtPassword.setText(pass);
            isRemember = true;
        }

        loginBinding.cbRememberMe.setOnClickListener(v -> {
            loginBinding.cbRememberMe.setChecked(!isRemember);
            isRemember = !isRemember;
        });

        loginBinding.tvResetPassword.setOnClickListener(v -> {
            showAlertDialog(LoginActivity.this);
        });

        loginBinding.btnLogin.setOnClickListener(v -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(LoginActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                CustomProgressDialog.getInstance().show(LoginActivity.this);
                if (isRemember) {
                    SessionManager.setData(SessionManager.USERNAME, loginBinding.edtUserName.
                            getText().toString());
                    SessionManager.setData(SessionManager.PASSWORD, loginBinding.edtPassword.
                            getText().toString());
                } else {
                    SessionManager.setData(SessionManager.USERNAME, "");
                    SessionManager.setData(SessionManager.PASSWORD, "");
                }

                if (loginBinding.edtUserName.getText().toString().trim().isEmpty()) {
                    vibrator.vibrate(1000);
                    loginBinding.edtUserName.startAnimation(shake);
                    FancyToast.makeText(LoginActivity.this, "UserName Empty",
                            FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
                    CustomProgressDialog.getInstance().dismiss();
                } else if (loginBinding.edtPassword.getText().toString().trim().isEmpty()) {
                    vibrator.vibrate(1000);
                    loginBinding.edtPassword.startAnimation(shake);
                    FancyToast.makeText(LoginActivity.this, "Password Empty",
                            FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
                    CustomProgressDialog.getInstance().dismiss();
                } else {
                    CommonFunctions.getInstance().HideSoftKeyboard(LoginActivity.this);
                    LoginApiResponse apiResponse = new LoginApiResponse();
                    apiResponse.setUsername(loginBinding.edtUserName.getText().toString().trim());
                    apiResponse.setPassword(loginBinding.edtPassword.getText().toString().trim());
                    Gson gson = new Gson();
                    String InputData = gson.toJson(apiResponse);

                    CommonApiCalls.getInstance().login(LoginActivity.this,
                            InputData, new APICommonCallback.Listener() {
                                @Override
                                public void onSuccess(Object object) {
                                    LoginApiResponse body = (LoginApiResponse) object;
                                    SessionManager.getInstance().userDetails(body.getFirstName(),
                                            body.getToken(), body.getId().toString());
                                    Constants.userName = body.getUsername();
                                    FancyToast.makeText(LoginActivity.this, Constants.LoginSuccess,
                                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    new Handler().postDelayed(() ->
                                            CommonFunctions.getInstance().newIntent(LoginActivity.this,
                                                    HomeActivity.class, Bundle.EMPTY, true), DISPLAY_LENGTH);
                                }

                                @Override
                                public void onFailure(String reason) {
                                    FancyToast.makeText(LoginActivity.this, reason,
                                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    if (CustomProgressDialog.getInstance().isShowing()) {
                                        CustomProgressDialog.getInstance().dismiss();
                                    }

                                }
                            });

                }
            }/* else {
                vibrator.vibrate(1000);
                loginBinding.edtUserName.startAnimation(shake);
                loginBinding.edtPassword.startAnimation(shake);
                FancyToast.makeText(LoginActivity.this, "Check your username and password...!",
                        FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
                CustomProgressDialog.getInstance().dismiss();
            }*/
        });
    }

    private void showAlertDialog(LoginActivity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(R.string.forgotPassword);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_forgot_password);
        EditText edtForgotUserName = dialog.findViewById(R.id.edtForgotUserName);
        Button btnForgotSubmit = dialog.findViewById(R.id.btnForgotSubmit);

        btnForgotSubmit.setOnClickListener(view -> {
            if (!CommonFunctions.getInstance().CheckInternetConnection()) {
                FancyToast.makeText(LoginActivity.this, Constants.CheckInternet,
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            } else {
                if (edtForgotUserName.getText().toString().trim().isEmpty()) {
                    FancyToast.makeText(activity, "UserName Empty",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    ForgotPasswordApiResponse apiResponse = new ForgotPasswordApiResponse();
                    apiResponse.setUsername(edtForgotUserName.getText().toString().trim());
                    Gson gson = new Gson();
                    String InputData = gson.toJson(apiResponse);

                    CommonApiCalls.getInstance().ForgotPassword(activity,
                            InputData, new APICommonCallback.Listener() {
                                @Override
                                public void onSuccess(Object object) {
                                    ForgotPasswordApiResponse body = (ForgotPasswordApiResponse) object;
                                    System.out.println("response---" + body);
                                    FancyToast.makeText(activity, body.toString(),
                                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    CustomProgressDialog.getInstance().dismiss();
                                }

                                @Override
                                public void onFailure(String reason) {
                                    System.out.println("Fail-->" + reason);
                                    FancyToast.makeText(activity, reason,
                                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    CustomProgressDialog.getInstance().dismiss();
                                }
                            });
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager manager = getFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count == 1) {
            super.onBackPressed();
        }
        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
            }
            this.doubleBackToExitPressedOnce = true;
            FancyToast.makeText(LoginActivity.this, Constants.pleaseClickBackAgainToExit,
                    FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        }
    }

    @Override
    protected void onResume() {
        if (CustomProgressDialog.getInstance().isShowing()) {
            CustomProgressDialog.getInstance().dismiss();
        }
        super.onResume();
        loginBinding.cbRememberMe.setChecked(isRemember);
    }
}