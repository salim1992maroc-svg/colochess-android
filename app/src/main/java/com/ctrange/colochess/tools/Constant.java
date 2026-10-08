package com.ctrange.colochess.tools;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import com.ctrange.colochess.R;
import com.ctrange.colochess.SplashActivity;

import java.util.regex.Pattern;

public class Constant {

    private static Preferences prefManager;
    private static Dialog dialog;
    private static Dialog dialog_page;

    public static final String tab_payeer = "tab_payeer";
    public static final String tab_paypal = "tab_paypal";

    public static final String IS_LOGIN = "IsLogin";
    public static final String USER_BLOCKED = "user_blocked";
    public static final String USER_NAME = "user_name";
    public static final String UserCountry = "user_country";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_POINTS = "user_points";
    public static final String USER_REFFER_CODE = "user_reffer_code";
    public static final String REFER_CODE_WITH = "refer_code_with";
    public static final String USER_ID = "user_id";
    public static final String USER_PASSWORD = "password";
    public static final String LAST_TIME_ADD_TO_SERVER = "last_time_added";
    public static final String IS_UPDATE = "user_points";
    public static final String DEVICE_ID = "Device_id";
    public static final String device_id_count = "device_id_count";
    public static final String MULTIPLE_ACCOUNT = "multiple";
    public static final String ONSIGNALE_APP_ID = "onsignal_app_id";


    public static final String co_sign_up_bonus = "_sign_up_bonus";
    public static final String co_referral_points_to_add = "_referral_points_to_add";
    public static final String co_vpn = "_vpn";
    public static final String co_root = "_root";
    public static  String co_gameurl = "_gameurl";


    //Information Pages
    public static final String i_site_page = "i_site_page";
    public static final String i_rules_page = "i_rules_page";
    public static final String i_privacy_page = "i_privacy_page";
    public static final String i_contact_page = "i_contact_page";

    public static final String i_status_true = "i_status_true";
    public static final String i_logo_mess = "i_logo_mess";
    public static final String i_title_mess = "i_title_mess";
    public static final String i_desc_mess = "i_desc_mess";

    //-----------game val------------------------//
    public static String game_online = "prf_game_online";
    //-----------game val------------------------//



    public static void setString(Context context, String preKey, String setString) {
        if (prefManager == null) {
            prefManager = new Preferences(context);
        }
        prefManager.setString(preKey, setString);
    }

    public static String getString(Context context, String prefKey) {
        if (prefManager == null) {
            prefManager = new Preferences(context);
        }
        return prefManager.getString(prefKey);
    }

    public static void setInt(Context context, String preKey, int setInt) {
        if (prefManager == null) {
            prefManager = new Preferences(context);
        }
        prefManager.setInt(preKey, setInt);
    }

    public static int getInt(Context context, String prefKey) {
        if (prefManager == null) {
            prefManager = new Preferences(context);
        }
        return prefManager.getInt(prefKey);
    }

    public static void remove(Context context, String PREF_NAME) {
        if (prefManager == null) {
            prefManager = new Preferences(context);
        }
        prefManager.remove(PREF_NAME);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void InternetErrorDialog(Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.internet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btn_connection_retry = dialog.findViewById(R.id.btn_connection_retry);
        Button btn_connection_exit = dialog.findViewById(R.id.btn_connection_exit);


        btn_connection_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SplashActivity.class));
            }
        });

        btn_connection_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    public static void showToastMessage(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }



    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean isMatches = pattern.matcher(email).matches();
        Log.e("Boolean Value", "" + isMatches);
        return isMatches;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showDialogPageLoading(Context context) {
        dialog_page = new Dialog(context);
        dialog_page.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_page.setContentView(R.layout.loading_page);
        dialog_page.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_page.setCancelable(false);
        dialog_page.show();
    }
    public static void hideDialogPageLoading(Context context){
        dialog_page.dismiss();
    }

    public static void GotoNextActivity(Context context, Class nextActivity, String msg) {
        if (context != null && nextActivity != null) {
            if (msg == null) {
                msg = "";
            }
            Intent intent = new Intent(context, nextActivity);
            intent.putExtra("Intent", msg);
            context.startActivity(intent);
        }
    }

    public static String extention = ".php";
    public static String u= "aHR0cDovL2xvY2FsaG9zdC9hZG1pbi9hcGkv";
}
