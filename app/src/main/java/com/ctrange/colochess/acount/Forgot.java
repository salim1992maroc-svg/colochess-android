package com.ctrange.colochess.acount;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.Constant.hideKeyboard;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.ctrange.colochess.SplashActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.ctrange.colochess.App;
import com.ctrange.colochess.R;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Forgot extends AppCompatActivity {

    Activity activity;
    private TextInputEditText email_EditText;
    private Button btn_goo_login;
    private ProgressDialog alertDialog;
    private String OTP;
    private LinearLayout otpLyt;

    AppCompatButton reset_btn;
    SweetAlertDialog pDialog;

    ImageView login_back;
    TextView tv_message;
    LinearLayout header_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot);
        activity = this;

        btn_goo_login = findViewById(R.id.btn_goo_login);
        header_msg = findViewById(R.id.header_msg);
        login_back = findViewById(R.id.login_back);
        email_EditText = findViewById(R.id.reset_email_edit_text);
        reset_btn = findViewById(R.id.reset_password_btn);
        tv_message = findViewById(R.id.tv_message);
        otpLyt = findViewById(R.id.reset_lyt_otp);
        alertDialog = new ProgressDialog(activity);
        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
        alertDialog.setTitle(getResources().getString(R.string.reset_password));
        alertDialog.setMessage(getResources().getString(R.string.please_wait));
        alertDialog.setCancelable(false);


        onClick();


    }

    private void onClick() {


        btn_goo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.setString(activity, Constant.IS_LOGIN,"false");
                startActivity(new Intent(activity, SplashActivity.class));
                finishAffinity();
            }
        });


        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    String Email = email_EditText.getText().toString();
                    if (Email.length() == 0) {
                        email_EditText.setError(getResources().getString(R.string.enter_email));
                        email_EditText.requestFocus();
                    } else if (!Constant.isValidEmailAddress(Email)) {
                        email_EditText.setError(getResources().getString(R.string.enter_valid_email));
                        email_EditText.requestFocus();
                    } else {
                        hideKeyboard(activity);
                        showProgressDialog();

                        resetPasswordEmail(Email,getRandomString(6));
                        reset_btn.setEnabled(false);
                        header_msg.setVisibility(View.VISIBLE);
                        login_back.setEnabled(false);
                    }
                } else {
                    Constant.InternetErrorDialog(activity);
                }
            }
        });

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

    private void resetPasswordEmail(String email, final String newpass) {

        showProgressDialog();

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("recover", "ok");
        params.put("email", email);
        params.put("newpass", newpass);

        Log.e("TAG", "resetPasswordEmail: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"forget"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    hideProgressDialog();

                    boolean status = response.getBoolean("status");

                    if (status) {

                        hideProgressDialog();

                        tv_message.setText(response.getString("message"));

                    } else {

                        tv_message.setText(response.getString("message"));

                        Constant.showToastMessage(activity, "This Email is Not Registered");
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
               // tv_message.setText(error.getMessage());
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



    public String getRandomString(int i){
        final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while(i>0){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString();
    }
}
