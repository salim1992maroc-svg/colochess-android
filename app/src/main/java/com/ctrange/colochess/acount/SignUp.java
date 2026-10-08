package com.ctrange.colochess.acount;



import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.Constant.hideKeyboard;
import static com.ctrange.colochess.tools.IPadress.getLocalIpAddress;
import static com.ctrange.colochess.tools.Vpn_Root.showmultipleDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ctrange.colochess.App;
import com.ctrange.colochess.R;
import com.ctrange.colochess.modul.User;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import com.ctrange.colochess.tools.getCountryIsoByName;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Activity activity;
    private EditText name_edit_text, number_edit_text, email_edit_text, password_edit_text, reffreal_code_edit_text;
    private TextView login_text_view;
    private AppCompatButton sign_up_button;
    private ProgressDialog alertDialog;
    ImageView sign_back;
    String Name,Number,Email,Password,refercode,singup_bonus,referral_points_bonus;

    String MULTIPLE_ACCOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        activity = this;

        MULTIPLE_ACCOUNT = Constant.getString(activity,Constant.MULTIPLE_ACCOUNT);



        sign_back = findViewById(R.id.sign_back);
        name_edit_text = findViewById(R.id.sign_up_name_edit_text);
        number_edit_text = findViewById(R.id.sign_up_number_edit_text);
        password_edit_text = findViewById(R.id.sign_up_password_edit_text);
        email_edit_text = findViewById(R.id.sign_up_email_edit_text);
        sign_up_button = findViewById(R.id.sign_up_btn);
        login_text_view = findViewById(R.id.login_text);
        reffreal_code_edit_text = findViewById(R.id.sign_up_refer_code_edit_text);
        alertDialog = new ProgressDialog(activity);
        alertDialog.setTitle(getResources().getString(R.string.signup_in_progress));
        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
        alertDialog.setMessage(getResources().getString(R.string.please_wait));
        alertDialog.setCancelable(false);
        onInitView();

    }

    public void showProgressDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void onInitView() {

        login_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });

        sign_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });



        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                     Name = name_edit_text.getText().toString();
                     Number = number_edit_text.getText().toString();
                     Email = email_edit_text.getText().toString();
                     Password = password_edit_text.getText().toString();
                     refercode = reffreal_code_edit_text.getText().toString();

                    if (Name.length() == 0) {
                        name_edit_text.setError(getResources().getString(R.string.enter_name));
                        name_edit_text.requestFocus();
                    } else if (Number.length() == 0) {
                        number_edit_text.setError(getResources().getString(R.string.enter_number));
                        number_edit_text.requestFocus();
                    } else if (Email.length() == 0) {
                        email_edit_text.setError(getResources().getString(R.string.enter_email));
                        email_edit_text.requestFocus();
                    } else if (!Constant.isValidEmailAddress(Email)) {
                        email_edit_text.setError(getResources().getString(R.string.enter_valid_email));
                        email_edit_text.requestFocus();
                    } else if (Password.length() == 0) {
                        password_edit_text.setError(getResources().getString(R.string.enter_password));
                        password_edit_text.requestFocus();
                    } else if (Password.length() < 8) {
                        password_edit_text.setError("Password must be at least 8 characters");
                        password_edit_text.requestFocus();
                    } else if (Password.length() < 6) {
                        password_edit_text.setError(getResources().getString(R.string.enter_6_digit_password));
                        password_edit_text.requestFocus();
                    } else {
                         hideKeyboard(activity);
                         showProgressDialog();
                         singup_bonus = Constant.getString(activity,Constant.co_sign_up_bonus);
                         referral_points_bonus = Constant.getString(activity,Constant.co_referral_points_to_add);


                        checkDeviceID();


                    }
                } else {
                    Constant.InternetErrorDialog(activity);
                }
            }
        });

    }


    public void checkDeviceID(){

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("TAG","device_iddd" + "My ID is: " + androidId);

        String device_id_count = Constant.getString(activity,Constant.device_id_count);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("device_id", androidId);
        params.put("device_id_count", device_id_count);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"checkDeviceID"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");

                    if (status) {

                        JSONObject object = response.getJSONObject("0");

                        String deviceid = object.getString("device_id");

                        Log.i("TAG", "true: " + response.getString("message") + " | " + deviceid);

                        if_aoccount_on();

                    } else {

                        Log.i("TAG", "false: " +  response.getString("message"));

                        signupNewUser(Email, Password, Name, Number, refercode, singup_bonus, referral_points_bonus);
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
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


    private void if_aoccount_on(){


        if(MULTIPLE_ACCOUNT.equals("on")){

            Log.i("TAG", "MULTIPLE_ACCOUNT: " + MULTIPLE_ACCOUNT);

            showmultipleDialog(activity);

        }else{

            signupNewUser(Email, Password, Name, Number, refercode, singup_bonus, referral_points_bonus);
        }


    }

    private void signupNewUser(final String email,
                               final String password,
                               final String name,
                               final String phone,
                               final String refercode,
                               final String sign_up_bonus,
                               final String referral_points) {

        if (activity == null) {
            hideProgressDialog();
            return;
        }


        String ipAdress = getLocalIpAddress();

        String device_id_count = Constant.getString(activity,Constant.device_id_count);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String getCountry = getCountryIsoByName.getDeviceCountryCode(activity);
        Constant.setString(activity,Constant.UserCountry,getCountry);

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("TAG","device_iddd" + "My ID is: " + androidId);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("register", "ok");
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("password", password);
        params.put("t_sign_up_bonus", sign_up_bonus);
        params.put("t_referral_points", referral_points);
        params.put("t_getCountry", getCountry);
        params.put("t_ip_addr", ipAdress);
        params.put("t_registration_date", currentDateandTime);
        params.put("referral_code", name.replaceAll("\\s+", "") + phone.substring(0, 4));
        params.put("device_id", androidId);
        params.put("device_id_count", device_id_count);

        if (!refercode.isEmpty()) {
            params.put("referral_with", refercode);
        }
        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"registers"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");

                    if (status) {

                        Log.i("TAG", "true2: " +  response.getString("message"));

                        Constant.setString(activity, Constant.USER_PASSWORD, password);

                        final User user = new User(
                                name.trim(),
                                phone,
                                email,
                                sign_up_bonus,
                                refercode, "false",name.replaceAll("\\s+","") + phone.substring(0, 4),
                                androidId);


                        if (activity == null) {
                            hideProgressDialog();
                            return;
                        }


                        JSONObject object = response.getJSONObject("0");

                        if (object.has("id")) {
                            Constant.setString(activity, Constant.USER_ID, object.getString("id"));
                            Log.e("TAG", "onResponse: " + object.getString("id"));
                        }
                        if (user.getName() != null) {
                            Constant.setString(activity, Constant.USER_NAME, user.getName());
                            Log.e("TAG", "onDataChange: " + user.getName());
                        }
                        if (user.getNumber() != null) {
                            Constant.setString(activity, Constant.USER_NUMBER, user.getNumber());
                            Log.e("TAG", "onDataChange: " + user.getNumber());
                        }
                        if (user.getEmail() != null) {
                            Constant.setString(activity, Constant.USER_EMAIL, user.getEmail());
                            Log.e("TAG", "onDataChange: " + user.getEmail());
                        }
                        if (user.getPoints() != null) {
                            Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange: " + user.getPoints());
                        }
                        if (user.getReferraled_with() != null) {
                            Constant.setString(activity, Constant.REFER_CODE_WITH, user.getReferraled_with());
                            Log.e("TAG", "onDataChange: " + user.getReferraled_with());
                        }
                        if (user.getIsBLocked() != null) {
                            Constant.setString(activity, Constant.USER_BLOCKED, user.getIsBLocked());
                            Log.e("TAG", "onDataChange: " + user.getIsBLocked());
                        }
                        if (user.getUserReferCode() != null) {
                            Constant.setString(activity, Constant.USER_REFFER_CODE, user.getUserReferCode());
                            Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                        }
                        if (user.getDevice_id() != null) {
                            Constant.setString(activity, Constant.DEVICE_ID, user.getDevice_id());
                            Log.e("TAG", "onDataChange: " + user.getDevice_id());
                        }

                        Log.i("TAG", "true3: " +  response.getString("message"));

                        Log.d("TAG", "onDataChange: " + user.getDevice_id());

                        Toast.makeText(activity, "registration_success", Toast.LENGTH_SHORT).show();

                        hideProgressDialog();

                        Constant.setString(activity, Constant.IS_LOGIN, "true");
                        Constant.showToastMessage(activity, getResources().getString(R.string.registration_success));
                        ///Constant.GotoNextActivity(activity, Accept_Conditions.class, "");
                        startActivity(new Intent(activity,Accept_Conditions.class));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();



                    } else {

                        Constant.showToastMessage(activity, response.getString("message"));

                        Log.i("TAG", "false2: " +  response.getString("message"));
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
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




}
