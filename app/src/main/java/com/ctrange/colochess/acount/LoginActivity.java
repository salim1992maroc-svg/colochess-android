package com.ctrange.colochess.acount;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.Constant.hideKeyboard;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ctrange.colochess.App;
import com.ctrange.colochess.MainActivity;
import com.ctrange.colochess.R;
import com.ctrange.colochess.SplashActivity;
import com.ctrange.colochess.modul.User;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Activity activity;
    private EditText email_edit_text, password_edit_text;
    private AppCompatButton login_button;
    private TextView sign_up_text_view, forgot_textView;
    private ProgressDialog alertDialog;
    ImageView login_back;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        activity = this;



        login_back = findViewById(R.id.login_back);
        email_edit_text = findViewById(R.id.login_email_edit_text);
        password_edit_text = findViewById(R.id.login_password_edit_text);
        login_button = findViewById(R.id.login_btn);
        sign_up_text_view = findViewById(R.id.sign_up_text);
        forgot_textView = findViewById(R.id.forgot_text);
        alertDialog = new ProgressDialog(activity);
        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
        alertDialog.setTitle(getResources().getString(R.string.login_in_progress));
        alertDialog.setMessage(getResources().getString(R.string.please_wait));
        alertDialog.setCancelable(false);
        onInitView();



    }


    private void onInitView() {

        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constant.isNetworkAvailable(activity)) {

                    String Email = email_edit_text.getText().toString();
                    String Password = password_edit_text.getText().toString();

                    if (Email.length() == 0) {
                        email_edit_text.setError(getResources().getString(R.string.enter_email));
                        email_edit_text.requestFocus();

                    } else if (!Constant.isValidEmailAddress(Email)) {
                        email_edit_text.setError(getResources().getString(R.string.enter_valid_email));
                        email_edit_text.requestFocus();

                    } else if (Password.length() == 0) {
                        password_edit_text.setError(getResources().getString(R.string.enter_password));
                        password_edit_text.requestFocus();

                    } else if (Password.length() < 6) {
                        password_edit_text.setError(getResources().getString(R.string.enter_valid_number));
                        password_edit_text.requestFocus();

                    } else {
                        hideKeyboard(activity);
                        showProgressDialog();
                        signInWithEmailandPassword(Email, Password);
                    }
                } else {
                    Constant.InternetErrorDialog(activity);
                }
            }
        });

        sign_up_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_Login, SignUpFragment.newInstance()).addToBackStack(null).commit();
                startActivity(new Intent(activity, SignUp.class));

            }
        });

        forgot_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_Login, ForgotFragment.newInstance()).addToBackStack(null).commit();
                startActivity(new Intent(activity, Forgot.class));
            }
        });

    }

    private void signInWithEmailandPassword(String email, final String password) {

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("TAG","device_iddd" + "My ID is: " + androidId);

        if (activity == null) {
            hideProgressDialog();
            return;
        }

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("get_login", "ok");
        params.put("email", email);
        params.put("password", password);

        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"login"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "response_1" + response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONObject jsonObject = response.getJSONObject("0");
                        Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                        Constant.setString(activity, Constant.USER_PASSWORD, password);

                        final User user = new User(
                                jsonObject.getString("name"),
                                jsonObject.getString("phone"),
                                jsonObject.getString("email"),
                                jsonObject.getString("points"),
                                jsonObject.getString("referraled_with"),
                                jsonObject.getString("status"),
                                jsonObject.getString("referral_code"),
                                jsonObject.getString("device_id"));

                        Log.d("TAG", "_BLOCKED" + jsonObject.getString("status"));

                        if (activity == null) {
                            hideProgressDialog();
                            return;
                        }

                        if (user.getName() != null) {
                            Constant.setString(activity, Constant.USER_NAME, user.getName());
                            Log.e("TAG", "onDataChange" + user.getName());
                        }
                        if (user.getNumber() != null) {
                            Constant.setString(activity, Constant.USER_NUMBER, user.getNumber());
                            Log.e("TAG", "onDataChange" + user.getNumber());
                        }
                        if (user.getEmail() != null) {
                            Constant.setString(activity, Constant.USER_EMAIL, user.getEmail());
                            Log.e("TAG", "onDataChange" + user.getEmail());
                        }
                        if (user.getPoints() != null) {
                            Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange" + user.getPoints());
                        }
                        if (user.getReferraled_with() != null) {
                            Constant.setString(activity, Constant.REFER_CODE_WITH, user.getReferraled_with());
                            Log.e("TAG", "onDataChange" + user.getReferraled_with());
                        }
                        if (user.getIsBLocked() != null) {
                            Constant.setString(activity, Constant.USER_BLOCKED, user.getIsBLocked());
                            Log.e("TAG", "USER_BLOCKED" + user.getIsBLocked());
                        }
                        if (user.getUserReferCode() != null) {
                            Constant.setString(activity, Constant.USER_REFFER_CODE, user.getUserReferCode());
                            Log.e("TAG", "onDataChange" + user.getUserReferCode());
                        }

                        hideProgressDialog();

                        if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                            showBlockedDialog();
                        } else {
                            Constant.setString(activity, Constant.IS_LOGIN, "true");
                            Constant.showToastMessage(activity, getResources().getString(R.string.login_successfully));

                            updateUI();
                            
                            
                        }
                    } else {
                        Constant.showToastMessage(activity, "Invalid Email Or Password");
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();

                    Log.e("TAG", "onDataChange_00" +  e);
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

                Toast.makeText(activity, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

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

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateUI() {

        Constant.GotoNextActivity(activity, MainActivity.class, "");
        //overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();


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

                startActivity(new Intent(activity, SplashActivity.class));
                finish();
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

}