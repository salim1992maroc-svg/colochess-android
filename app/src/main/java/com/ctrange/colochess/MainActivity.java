package com.ctrange.colochess;



import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.im;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.Vpn_Root.rootChecker;
import static com.ctrange.colochess.tools.Vpn_Root.vpnChecker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ctrange.colochess.profile.Profile;
import com.ctrange.colochess.sdkoffers.IDS;
import com.ctrange.colochess.sdkoffers.Offers_Activity;
import com.ctrange.colochess.tools.getCountryIsoByName;
import com.ctrange.colochess.transaction.Transaction_Status;
import com.ctrange.colochess.characters.Character_Page;
import com.ctrange.colochess.sdkoffers.OfferSIds;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Activity activity;
    public WebView wb_view;
    private String wb_url;
    public static String points;
    private ProgressDialog alertDialog;
    Button bbtn_offers;

    String net_network_name,net_ids,net_place;
    ArrayList<OfferSIds> offerlist = new ArrayList<>();

    LottieAnimationView rltv_click;

    TextView btn_transaction,btn_characters;

    ImageView btn_profile;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        activity = this;


        if (Constant.isNetworkAvailable(this)) {

            wb_view = findViewById(R.id.wb_view);

            wb_url = Constant.getString(activity,Constant.co_gameurl);

            LoadPage(wb_url);

        } else {

            Constant.InternetErrorDialog(this);
        }



        btn_profile = findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(activity, Profile.class));

            }
        });

        btn_transaction = findViewById(R.id.btn_transaction);
        btn_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent transaction = new Intent(activity, Transaction_Status.class);
                startActivity(transaction);

            }
        });
        btn_characters = findViewById(R.id.btn_characters);
        btn_characters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent characters = new Intent(activity, Offers_Activity.class);
                startActivity(characters);

            }
        });

        String getCountry = getCountryIsoByName.getDeviceCountryCode(activity);
        Constant.setString(activity,Constant.UserCountry,getCountry);

        rltv_click = findViewById(R.id.rltv_click);
        rltv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(activity, Character_Page.class));


            }
        });

        timeer();
        dialogMessage();
        custom_offerwalls();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage(String Url) {
        wb_view.setWebViewClient(new MainActivity.MyWebViewClient());
        wb_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    Log.i("TAG","progressBar" + progress);
                } else {
                    Log.i("TAG","progressBar" + progress);
                }
            }
        });
        wb_view.getSettings().setLoadsImagesAutomatically(true);
        wb_view.getSettings().setJavaScriptEnabled(true);
        wb_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb_view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wb_view.getSettings().setDomStorageEnabled(true);
        wb_view.getSettings().setDatabaseEnabled(true);
        wb_view.getSettings().setGeolocationEnabled(true);
        wb_view.loadUrl(Url);

        wb_view.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view,String url) {

                CookieSyncManager.createInstance(wb_view.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                //cookieManager.removeSessionCookie();

                cookieManager.setCookie(url,"cookies-state=accepted");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                } else {
                    CookieSyncManager.getInstance().sync();
                }


                super.onPageFinished(view,url);
            }

        });

    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String myUrl) {
            view.loadUrl(myUrl);
            return false;
        }
    }


    private void custom_offerwalls() {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"ids"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject responseObj = response.getJSONObject(i);

                        net_network_name = responseObj.getString("network_name");
                        net_ids = responseObj.getString("app_ids");
                        net_place = responseObj.getString("app_placement");

                        offerlist.add(new OfferSIds(net_network_name,net_ids,net_place));

                      // Log.i("TAG","nnnnnnnnn: " + net_network_name +"-"+ net_ids +"-"+ net_place);

                    } catch (JSONException e) {
                        //Toast.makeText(activity, "JSONException:|" + e, Toast.LENGTH_SHORT).show();
                        Log.i("TAG","getMessage: " + e.getMessage());
                    }
                }

                try {


                if(net_ids != null && net_place != null) {

                    for (OfferSIds offer : offerlist) {

                        if (offer == null || offer.getNetwork_name() == null) {
                            continue;
                        }

                        String networkName = offer.getNetwork_name();
                        String ids = offer.getIds_();
                        String placement = offer.getPlacem();

                        if ("TapJoy".equalsIgnoreCase(networkName)) {

                            Constant.setString(activity, IDS.tapjoy_network_id, ids);
                            Constant.setString(activity, IDS.tapjoy_placement_id, placement);

                        } else if ("IronSource".equalsIgnoreCase(networkName)) {

                            Constant.setString(activity, IDS.iron_network_id, ids);
                            Constant.setString(activity, IDS.iron_placement_id, placement);

                        } else if ("OkSpin".equalsIgnoreCase(networkName)) {

                            Constant.setString(activity, IDS.app_key_okspin, ids);
                            Constant.setString(activity, IDS.place_okspin, placement);
                        }
                    }



                    Log.i("TAG","ids: " + Constant.getString(activity,IDS.tapjoy_network_id));
                    Log.i("TAG","ids: " + Constant.getString(activity,IDS.iron_network_id));
                     Log.i("TAG","ids: " + Constant.getString(activity,IDS.app_key_okspin));






                }

                }catch (Exception e){
                    Log.i("TAG","biiiii: " + e.getMessage());
                   // Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity, "VolleyError:|" + error, Toast.LENGTH_SHORT).show();
                Log.i("TAG","error_getMessage: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void dialogMessage() {

        alertDialog = new ProgressDialog(activity);
        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
        alertDialog.setTitle("Data processing");
        alertDialog.setMessage("please wait..");
        alertDialog.setCancelable(false);

        showProgressDialog();

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("dialog", "ok");


        Log.e("TAG", "dialogmsg: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"dialogmsg"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "vvvvvvv" + response.toString());

                try {


                    boolean status = response.getBoolean("status");

                    if (status) {

                        //Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject object = response.getJSONObject("0");

                        if (object.has("dlog_title")) {
                            Constant.setString(activity, Constant.i_title_mess, object.getString("dlog_title"));
                            Log.d("TAG", "dlog_title: " + object.getString("dlog_title"));

                        }
                        if (object.has("dlog_mess")) {
                            Constant.setString(activity, Constant.i_desc_mess, object.getString("dlog_mess"));
                            Log.d("TAG", "dlog_mess: " + object.getString("dlog_mess"));
                        }
                        if (object.has("dlog_image")) {
                            Constant.setString(activity, Constant.i_logo_mess, object.getString("dlog_image"));
                            Log.d("TAG", "logo_mess: " + object.getString("dlog_image"));
                        }
                        if (object.has("statu")) {
                            Constant.setString(activity, Constant.i_status_true, object.getString("statu"));
                            Log.d("TAG", "statu: " + object.getString("statu"));
                        }

                        showdialogmessage();

                    } else {

                        hideProgressDialog();
                        Constant.showToastMessage(activity, response.getString("message"));
                    }

                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                    Log.d("TAG", "JSONE_xception: " + e.getMessage());
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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void showdialogmessage(){

        String isTrue = Constant.getString(activity,Constant.i_status_true);

       // Toast.makeText(activity, isTrue, Toast.LENGTH_SHORT).show();

        Log.i("TAG","isTrue: " + isTrue);

        if(isTrue.equals("1")){

            dialogMessg();

        }

    }

    public void dialogMessg() {

        hideProgressDialog();

        String logo = Constant.getString(activity,Constant.i_logo_mess);
        String title = Constant.getString(activity,Constant.i_title_mess);
        String desc = Constant.getString(activity,Constant.i_desc_mess);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView logo_dialog_mess = dialog.findViewById(R.id.logo_dialog_mess);
        TextView tv_title_dialog_mess = dialog.findViewById(R.id.tv_title_dialog_mess);
        TextView desc_dialog_mess = dialog.findViewById(R.id.desc_dialog_mess);
        Button btn_message_ok = dialog.findViewById(R.id.btn_message_ok);

        Picasso.get().load(unt()+im+logo).into(logo_dialog_mess);
        tv_title_dialog_mess.setText(title);
        desc_dialog_mess.setText(desc);

        ImageView exite_message = dialog.findViewById(R.id.exite_message);
        exite_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_message_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
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

    public void timeer(){

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                rltv_click.setVisibility(View.VISIBLE);
                rltv_click.setVisibility(View.VISIBLE);
                btn_profile.setVisibility(View.VISIBLE);



            }
        }.start();


    }

    public static String hhu(){
        String ss = "ZmlsZTovLy9hbmRyb2lkX2Fzc2V0L3BocC5waHA=";
        byte[] dataa = Base64.decode(ss, Base64.DEFAULT);
        return new String(dataa, StandardCharsets.UTF_8);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vpnChecker(activity);
        rootChecker(activity);
    }
}