package com.ctrange.colochess;


import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ctrange.colochess.acount.Accept_Conditions;
import com.ctrange.colochess.acount.LoginActivity;
import com.ctrange.colochess.modul.PagesModuls;
import com.ctrange.colochess.modul.User;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    boolean LOGIN = false;
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE = 101;
    Activity activity;
    String user_name = null;

    public User user;
    ImageView btn_start2;

    TextView tv_scan_desc;

    LottieAnimationView scan_anim;

    String site_page,rules_page,privacy_page,contact_page;
    ArrayList<PagesModuls> pages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        activity = this;


        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);

        if (Constant.isNetworkAvailable(activity)) {

            pages_information();
            getcontroller();
            getTabValue();

        } else {

            Constant.InternetErrorDialog(activity);
        }

        tv_scan_desc = findViewById(R.id.tv_scan_desc);

        btn_start2 = findViewById(R.id.btn_start2);


        String is_login = Constant.getString(activity, Constant.IS_LOGIN);

       // Toast.makeText(activity, is_login, Toast.LENGTH_SHORT).show();

        if (is_login.equals("true")) {
            LOGIN = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "onCreate:if part activarte ");
            appUpdateManager = AppUpdateManagerFactory.create(activity);
            UpdateApp();

        } else {

            Log.e("TAG", "onCreate:else part activarte ");

            onInit();

        }

        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.GotoNextActivity(activity, MainActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();


            }
        });




    }

    private void onInit() {

        if (Constant.isNetworkAvailable(activity)) {

            if (LOGIN) {


                try {

                    JSONObject requestBody = new JSONObject();
                    requestBody.put("email", Constant.getString(activity, Constant.USER_EMAIL));
                    requestBody.put("password", Constant.getString(activity, Constant.USER_PASSWORD));

                    String loginUrl = unt() + "api/auth/login";
                    Log.d("TAG", "Cloudflare splash login URL: " + loginUrl);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            loginUrl, requestBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", "Cloudflare splash login response: " + response.toString());

                            try {
                                if (response.optBoolean("success", false)) {
                                    JSONObject jsonUser = response.optJSONObject("user");
                                    JSONObject wallet = response.optJSONObject("wallet");
                                    if (jsonUser == null) throw new JSONException("Missing user object");

                                    Constant.setString(activity, Constant.USER_ID, jsonUser.optString("id", ""));
                                    String name = jsonUser.optString("first_name", "");
                                    if (name.isEmpty()) name = jsonUser.optString("username", "");
                                    Constant.setString(activity, Constant.USER_NAME, name);
                                    Constant.setString(activity, Constant.USER_NUMBER, "");
                                    Constant.setString(activity, Constant.USER_EMAIL, jsonUser.optString("email", ""));
                                    Constant.setString(activity, Constant.USER_POINTS,
                                            wallet != null ? String.valueOf(wallet.optLong("balance", 0)) : "0");
                                    Constant.setString(activity, Constant.REFER_CODE_WITH, jsonUser.optString("referred_by", ""));
                                    Constant.setString(activity, Constant.USER_BLOCKED, "1");
                                    Constant.setString(activity, Constant.USER_REFFER_CODE, jsonUser.optString("referral_code", ""));

                                    if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                                        showBlockedDialog();
                                    } else {
                                        Log.e("TAG", "onInit: login part");
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                tv_scan_desc.setText("Done!");
                                                btn_start2.setVisibility(View.VISIBLE);
                                            }
                                        }, 1000);
                                    }
                                } else {
                                    Log.e("TAG", "onInit: Cloudflare login failed");
                                    Constant.setString(activity, Constant.IS_LOGIN, "");
                                    Constant.GotoNextActivity(activity, Accept_Conditions.class, "");
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }
                            } catch (JSONException e) {
                                Log.e("TAG", "Cloudflare splash login parse error", e);
                                Constant.setString(activity, Constant.IS_LOGIN, "");
                                Constant.GotoNextActivity(activity, LoginActivity.class, "");
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", "Cloudflare splash login error", error);
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            1000 * 20,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    App.getInstance().addToRequestQueue(jsonObjReq, "cloudflare_splash_login_req");
                } catch (Exception e) {
                    Log.e("TAG", "onInit: excption " + e.getMessage().toString());
                }
            } else {

                if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                    showBlockedDialog();
                    return;
                }

                Log.e("TAG", "onInit: else part of no login");
                Constant.GotoNextActivity(activity, LoginActivity.class, "");
               // overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        } else {
            Constant.InternetErrorDialog(activity);
        }


    }

    public void UpdateApp() {


        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                            Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "onCreate:startUpdateFlowForResult else part activarte ");
                        onInit();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onCreate:addOnFailureListener else part activarte ");
                    onInit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        //for ironSource
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);


        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                onInit();
            } else {
                onInit();
            }
        }
    }

    private void getcontroller() {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"controller"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                    try {

                        JSONObject responseObj = response.getJSONObject(0);

                        String sign_up_bonus = responseObj.getString("c_sign_up_bonus");
                        String referral_points = responseObj.getString("c_referral_points");
                        String vpn = responseObj.getString("c_vpn");
                        String root = responseObj.getString("c_root");
                        String onsignale_app_id = responseObj.getString("onsignale_app_id");
                        String multiple = responseObj.getString("c_multiple");
                        String device_id_count = responseObj.getString("device_id_count");
                        String gameurl = responseObj.getString("game_url");


                            Constant.setString(activity, Constant.co_sign_up_bonus, sign_up_bonus);
                            Constant.setString(activity, Constant.co_referral_points_to_add, referral_points);
                            Constant.setString(activity, Constant.co_vpn, vpn);
                            Constant.setString(activity, Constant.co_root, root);
                            Constant.setString(activity, Constant.ONSIGNALE_APP_ID, onsignale_app_id);
                            Constant.setString(activity, Constant.MULTIPLE_ACCOUNT, multiple);
                            Constant.setString(activity, Constant.device_id_count, device_id_count);
                            Constant.setString(activity, Constant.co_gameurl, gameurl);

                           // Log.d("TAG","controller " + sign_up_bonus + " " + gameurl + " " + vpn + " " + root);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }



    public void showBlockedDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.block_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btn_retry = dialog.findViewById(R.id.btn_blocked_retry);
        Button btn_exit = dialog.findViewById(R.id.btn_blocked_exit);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(activity,SplashActivity.class));

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.show();
    }

    private void pages_information() {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"pagesinfo"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {



                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject responseObj = response.getJSONObject(i);

                        site_page = responseObj.getString("site_page");
                        rules_page = responseObj.getString("rules_page");
                        privacy_page = responseObj.getString("privacy_page");
                        contact_page = responseObj.getString("contact_page");

                       // pages.add(new PagesModuls(site_page,rules_page,privacy_page,contact_page));

                         Constant.setString(activity, Constant.i_site_page, site_page);
                         Constant.setString(activity, Constant.i_rules_page, rules_page);
                         Constant.setString(activity, Constant.i_privacy_page, privacy_page);
                         Constant.setString(activity, Constant.i_contact_page, contact_page);


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(activity, "pages" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG","pagesException " + e.getMessage());
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "mages2" + error, Toast.LENGTH_SHORT).show();
                Log.d("TAG","pagesException2 " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }


    private void getTabValue() {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"tab"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try {

                    JSONObject responseObj = response.getJSONObject(0);

                    String T_payeer = responseObj.getString("tab_payeer");
                    String T_paypal = responseObj.getString("tab_paypal");

                    Constant.setString(activity, Constant.tab_payeer, T_payeer);
                    Constant.setString(activity, Constant.tab_paypal, T_paypal);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

}