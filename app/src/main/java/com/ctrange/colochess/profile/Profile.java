package com.ctrange.colochess.profile;


import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.im;
import static com.ctrange.colochess.App.unt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ctrange.colochess.App;
import com.ctrange.colochess.PrivacyActivity;
import com.ctrange.colochess.SplashActivity;
import com.ctrange.colochess.acount.Forgot;
import com.ctrange.colochess.acount.SignUp;
import com.ctrange.colochess.modul.User;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.R;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Profile extends AppCompatActivity {

    Activity activity;
    public User user;
    private Dialog inputDiag;
    String mCountry,refferall_code;
    TextView profile_countryView,codeView;
    Button profile_new_passBtn,profile_codeBtn;

    EditText profile_codeInput;

    SweetAlertDialog pDialog;

    ImageView sign_back_profile;
    TextView profile_go_logout;
    TextView nameView;
    private int type;
    ImageView profile_avatarView;
    TextView profile_emailView;
    String username;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profile_activity);
        activity=Profile.this;

        //image Avatar
        profile_avatarView = findViewById(R.id.profile_avatarView);
        Picasso.get().load(unt()+im+"user_avatar.png").into(profile_avatarView);

        //country
        profile_countryView = findViewById(R.id.profile_countryView);
        mCountry = Constant.getString(activity,Constant.UserCountry);
        profile_countryView.setText(mCountry);

        //email
        profile_emailView = findViewById(R.id.profile_emailView);
        profile_emailView.setText(Constant.getString(activity,Constant.USER_EMAIL));

        //change pass
        profile_new_passBtn = findViewById(R.id.profile_new_passBtn);
        profile_new_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, Forgot.class));
            }
        });

        //refferall code
        profile_codeInput = findViewById(R.id.profile_codeInput);
        codeView = findViewById(R.id.profile_codeView);
        refferall_code = Constant.getString(activity,Constant.USER_REFFER_CODE);
        codeView.setText(refferall_code);

        codeView.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Copied", codeView.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Profile.this, clip.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
        profile_codeBtn = findViewById(R.id.profile_codeBtn);
        String refferal_with = Constant.getString(activity,Constant.REFER_CODE_WITH);
        profile_codeInput.setText(refferal_with);
        profile_codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (refferal_with.equals("")) {

                    if (Constant.isNetworkAvailable(activity)) {

                            String refercode = profile_codeInput.getText().toString();

                            loading();

                            referral(refercode);

                    } else {
                        Constant.InternetErrorDialog(activity);
                    }

                }else{

                    Toast.makeText(activity, "You have already redeemed the referral code.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sign_back_profile = findViewById(R.id.sign_back_profile);
        sign_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });
        profile_go_logout = findViewById(R.id.profile_go_logout);
        profile_go_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                Constant.setString(activity, Constant.IS_LOGIN,"false");
                startActivity(new Intent(activity, SplashActivity.class));
                finishAffinity();
            }
        });
        nameView = findViewById(R.id.profile_nameView);
        username = Constant.getString(activity,Constant.USER_NAME);
        nameView.setText(username);
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                inputDiag();
            }
        });
        TextView profile_go_delete = findViewById(R.id.profile_go_delete);
        profile_go_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                deleteUser();

                startActivity(new Intent(activity, SignUp.class));
                finishAffinity();

            }
        });
        TextView btn_privacy = findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(activity, PrivacyActivity.class);
                c.putExtra("Intent",Constant.getString(activity, Constant.i_privacy_page));
                startActivity(c);

            }
        });
        TextView btn_terms = findViewById(R.id.btn_terms);
        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent b = new Intent(activity,PrivacyActivity.class);
                b.putExtra("Intent",Constant.getString(activity, Constant.i_rules_page));
                startActivity(b);

            }
        });

    }
    private void inputDiag() {
        if (inputDiag == null) {
            inputDiag = decoratedDiag(this, R.layout.dialog_profile, 0.6f);

            inputDiag.setCancelable(true);
            inputDiag.setCanceledOnTouchOutside(true);

            TextView titleView = inputDiag.findViewById(R.id.dialog_profile_titleView);
            TextView inputView = inputDiag.findViewById(R.id.dialog_profile_inputView);

            if (type == 2) {
                titleView.setText(getString(R.string.change_name));
                inputView.setText(getString(R.string.enter_name));
            }
            EditText inputEdit = inputDiag.findViewById(R.id.dialog_profile_inputEdit);
            Button btn = inputDiag.findViewById(R.id.dialog_profile_btn);

            btn.setOnClickListener(view -> {
                btn.setText(getString(R.string.updating));
                String text = inputEdit.getText().toString();
                if (type == 2) {
                    if (text.isEmpty()) {
                        btn.setText(getString(R.string.update));
                        Toast.makeText(Profile.this, "Enter your name", Toast.LENGTH_LONG).show();
                        return;
                    }
                    inputDiag.dismiss();
                    btn.setText(getString(R.string.update));
                   //change(text);
                    changeUserName(text);
                    //Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                    nameView.setText(text);
                }
            });
        } else {
            TextView titleView = inputDiag.findViewById(R.id.dialog_profile_titleView);
            TextView inputView = inputDiag.findViewById(R.id.dialog_profile_inputView);
            EditText e = inputDiag.findViewById(R.id.dialog_profile_inputEdit);
            e.setText("");
            if (type == 2) {
                titleView.setText(getString(R.string.change_name));
                inputView.setText(getString(R.string.enter_name));
            }
        }
        inputDiag.show();
    }

    public void changeUserName(String userName) {

        // loading();
        Constant.showDialogPageLoading(activity);

        // tv_name.setText("...");
        //  tv_country.setText("...");
       // nameView.setText("...");

        String userID = Constant.getString(activity,Constant.USER_ID);
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("change_username","ok");
        params.put("user_id",userID);
        params.put("username",userName);

        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"changeusername"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "onDataChange: " + response.toString());

                try {

                    boolean status = response.getBoolean("status");

                    if (status) {
                        Log.e("TAG", "onDataChange: " + status);
                        Log.e("TAG", "onDataChange: " + response.getString("message"));
                        Constant.hideDialogPageLoading(activity);

                        JSONObject object = response.getJSONObject("0");

                          String userName = object.getString("name");

                          Constant.setString(activity, Constant.USER_NAME, userName);
                          Log.e("TAG", "onDataChange: " + userName);



                    }else{

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onDataChange: " + response.getString("message"));
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, activity.getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public static Dialog decoratedDiag(Context context, int layout, float opacity) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        View view = LayoutInflater.from(context).inflate(layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = opacity;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        w.setAttributes(lp);
        w.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(ContextCompat.getColor(context, R.color.purple_200));
            w.setNavigationBarColor(ContextCompat.getColor(context, R.color.purple_500));
        }
        return dialog;
    }

    private void referral(final String refercode){

        String referral_points_bonus = Constant.getString(activity,Constant.co_referral_points_to_add);

        String userName = Constant.getString(activity,Constant.USER_NAME);
        String userPhone = Constant.getString(activity,Constant.USER_NUMBER);
        String uuid = Constant.getString(activity,Constant.USER_ID);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("referral", "ok");
        params.put("user_id", uuid);
        params.put("t_referral_points", referral_points_bonus);
        params.put("referral_code", userName.replaceAll("\\s+", "") + userPhone.substring(0, 4));

        if (!refercode.isEmpty()) {
            params.put("referral_with", refercode);
        }

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"referral"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG","onRespo: "+ response.toString());

                try {
                    pDialog.dismissWithAnimation();

                    boolean status = response.getBoolean("status");

                    if (status) {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();

                        Log.d("TAG", "statusss: " + response.getString("message"));

                        JSONObject jsonObject = response.getJSONObject("0");

                        Constant.setString(activity, Constant.REFER_CODE_WITH, jsonObject.getString("referraled_with"));

                        String refferal_with = Constant.getString(activity,Constant.REFER_CODE_WITH);

                        profile_codeBtn.setEnabled(false);
                        profile_codeInput.setText(refferal_with);
                        pDialog.dismissWithAnimation();

                     // Constant.showToastMessage(activity, response.getString("message"));

                    } else {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismissWithAnimation();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void deleteUser(){

        String uuid = Constant.getString(activity,Constant.USER_ID);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("delete_user", "ok");
        params.put("user_id", uuid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"delete_user"+ex, params, new Response.Listener<JSONObject>() {

            @SuppressLint("ServiceCast")
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG","onRespo: "+ response.toString());

                try {

                    pDialog.dismissWithAnimation();

                    boolean status = response.getBoolean("status");

                    if (status) {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();


                        ((ActivityManager)getSystemService(ACCOUNT_SERVICE)).clearApplicationUserData();

                    } else {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismissWithAnimation();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    public void loading(){

        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        username = Constant.getString(activity,Constant.USER_NAME);
    }
}
