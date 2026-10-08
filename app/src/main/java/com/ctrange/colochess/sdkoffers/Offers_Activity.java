package com.ctrange.colochess.sdkoffers;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.IPadress.getLocalIpAddress;
import static com.ctrange.colochess.tools.Vpn_Root.rootChecker;
import static com.ctrange.colochess.tools.Vpn_Root.vpnChecker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import com.ctrange.colochess.App;
import com.ctrange.colochess.PrivacyActivity;
import com.ctrange.colochess.R;
import com.ctrange.colochess.offers_trans.Offers_Transaction_Status;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import com.ctrange.colochess.tools.getCountryIsoByName;
import com.ctrange.colochess.transaction.Transaction_Status;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJEarnedCurrencyListener;
import com.tapjoy.TJError;
import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.gamify.space.Gamify;
import com.gamify.space.GamifyCallback;
import com.gamify.space.GamifyError;

public class Offers_Activity extends AppCompatActivity {

    public static TJPlacement p_offers;

    int delay_offers;

    String userid;
    //tapjoy
    String tapjoy_id,tapjoy_placement;
    //ironsource
    String app_id,placement_id;
    //okspin
    String place_okspin,appkey_okspin;
    //bitlabs
    String token_bitlap;
    //MM Wall
    String mmwall_publisherid;


    private Offers_Activity activity;

    //start for sdkofferwalls list
    RecyclerView sdkrecyclerView_offers;
    AdapterSdkOffers sdkadapterSdkOffers_offers;
    ArrayList<ModelList> sdkarraymodelLists_offers;
    ProgressBar sdkprogressBar_offers;
    String s_net_image_offers,s_net_name_offers,s_net_desc_offers,s_net_app_ids_offers,s_net_placement_offers,s_type_offers;
    //end for sdkofferwalls list
    PopupMenu popupMenu;
    ImageView popup_offers;

    SweetAlertDialog pDialog_offers;

    TextView tv_name_offers,tv_country_offers,tv_balance_offers;

    public static MediaPlayer btnClick_offers,fatal_errer_offers;

    ImageView go_back_offers;

    ImageView refresh;

    TextView btn_offers_trans;

    ModelList modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.offers_activity);
        activity=this;

        btnClick_offers = MediaPlayer.create(activity,R.raw.click);
        fatal_errer_offers = MediaPlayer.create(activity,R.raw.fatal_error);
        popup_offers = findViewById(R.id.popupMenu_offers);
        popupMenu = new PopupMenu(activity, popup_offers);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setForceShowIcon(true);

        //userid
        userid = Constant.getString(activity,Constant.USER_ID);

        //tapjoy
        tapjoy_id = Constant.getString(activity, IDS.tapjoy_network_id);
        tapjoy_placement = Constant.getString(activity, IDS.tapjoy_placement_id);
        Log.i("TAG","all_ids: " + tapjoy_id +" | "+ tapjoy_placement);

        //ironsource
        app_id = Constant.getString(activity, IDS.iron_network_id);
        placement_id = Constant.getString(activity, IDS.iron_placement_id);
        String appKey = Constant.getString(activity,IDS.iron_network_id);
        IronSource.setUserId(userid);
        Log.i("TAG","all_ids: " + appKey +" | "+ placement_id );
        IronSource.init(this, appKey, IronSource.AD_UNIT.OFFERWALL);

        //okspin
        place_okspin = Constant.getString(activity, IDS.place_okspin);
        appkey_okspin = Constant.getString(activity, IDS.app_key_okspin);

        // Initialize OkSpin/Gamify before the user can open its GSpace.
        initGamify();


        tv_name_offers = findViewById(R.id.tv_name_offers);
        tv_country_offers = findViewById(R.id.tv_country_offers);
        tv_balance_offers = findViewById(R.id.tv_balance_offers);

        String user_name = Constant.getString(activity,Constant.USER_NAME);
        String country = Constant.getString(activity,Constant.UserCountry);
        Log.i("TAG","user_and_country: " + user_name + ":---:" + country);
        tv_name_offers.setText(user_name);
        tv_country_offers.setText(country);

        popup_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pup();

            }
        });
        go_back_offers = findViewById(R.id.go_back_offers);
        go_back_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });

        refresh = findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_select();

            }
        });
        btn_offers_trans = findViewById(R.id.btn_offers_trans);
        btn_offers_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(activity, Offers_Transaction_Status.class));

               /*
                String uuu = "custom_offerwall_HFKUED";
                String sss = String.valueOf(uuu.toLowerCase().contains("custom_offerwall_"));
                String OfferWall_Url = uuu.replace("custom_offerwall_", "");
                Toast.makeText(activity,OfferWall_Url , Toast.LENGTH_SHORT).show();
                */

            }
        });

        sdkofferwalllist();

    }

    private void getSdkData() {

        RequestQueue queue = Volley.newRequestQueue(activity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"customofferwalls"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                sdkprogressBar_offers.setVisibility(View.GONE);
                sdkrecyclerView_offers.setVisibility(View.VISIBLE);

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject responseObj = response.getJSONObject(i);

                        s_net_name_offers = responseObj.getString("net_name");
                        s_net_desc_offers = responseObj.getString("net_desc");
                        s_net_image_offers = responseObj.getString("net_image");
                        s_net_app_ids_offers = responseObj.getString("net_app_ids");
                        s_net_placement_offers = responseObj.getString("net_placement");
                        s_type_offers = responseObj.getString("type");

                        sdkarraymodelLists_offers.add(new ModelList(
                                s_net_name_offers,
                                s_net_desc_offers,
                                s_net_image_offers,
                                s_net_app_ids_offers,
                                s_net_placement_offers,
                                s_type_offers));

                        Log.i("TAG","raaaaaa" + " : " + s_net_name_offers + " : " + s_net_desc_offers + " : " + s_net_image_offers + " : " + s_net_app_ids_offers + " : " + s_net_placement_offers + " : " + s_type_offers) ;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RedeemActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void loading(){

        pDialog_offers = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog_offers.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog_offers.setTitleText("Loading ...");
        pDialog_offers.setCancelable(true);
        pDialog_offers.show();

    }

    private void initGamify() {
        Gamify.setCallback(new GamifyCallback() {
            @Override public void onInitSuccess() {
                Log.i("Gamify", "SDK init success");
            }
            @Override public void onInitFailed(GamifyError error) {
                Log.e("Gamify", "SDK init failed: " + error);
            }
            @Override public void onBannerReady(String placementId) {}
            @Override public void onBannerLoadFailed(String placementId, GamifyError error) {}
            @Override public void onBannerShowFailed(String placementId, GamifyError error) {}
            @Override public void onBannerClick(String placementId) {}
            @Override public void onInterstitialOpen(String placementId) {}
            @Override public void onInterstitialOpenFailed(String placementId, GamifyError error) {}
            @Override public void onInterstitialClose(String placementId) {}
            @Override public void onOfferWallOpen(String placementId) {}
            @Override public void onOfferWallOpenFailed(String placementId, GamifyError error) {}
            @Override public void onOfferWallClose(String placementId) {}
            @Override public void onGSpaceOpen(String placementId) {}
            @Override public void onGSpaceOpenFailed(String placementId, GamifyError error) {}
            @Override public void onGSpaceClose(String placementId) {}
            @Override public void onUserInteraction(String placementId, String interaction) {}
        });
        Gamify.initSDK(appkey_okspin);
        Gamify.setUserId(userid);
    }

    private void sdkofferwalllist(){

        // user_select();

        // OKSPIN TEMPORARILY DISABLED: SDK dependency is unavailable in CI.
        // Re-enable by restoring the OkSpin dependency/imports and calling okspiin().
        SetUpTapJoy();
        ironofferwall();

        sdkprogressBar_offers = findViewById(R.id.sdkidPB_offers);
        sdkrecyclerView_offers = findViewById(R.id.sdkrecy_offers_offers);

        RecyclerView.LayoutManager layoutManager = null;

        // this is the default;
        // this call is actually only necessary with custom ItemAnimators
        sdkrecyclerView_offers.setItemAnimator(new DefaultItemAnimator());
        sdkarraymodelLists_offers = new ArrayList<>();

        getSdkData();
        sdkadapterSdkOffers_offers = new AdapterSdkOffers(sdkarraymodelLists_offers,activity);
        /*LinearLayoutManager to show a staggered grid view. We can specify number of columns in the grid.*/
        //spanCount:   If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
        //orientation: StaggeredGridLayoutManager.HORIZONTAL or StaggeredGridLayoutManager.HORIZONTAL
        layoutManager = new StaggeredGridLayoutManager(2/*span count*/, StaggeredGridLayoutManager.VERTICAL/* orientation*/);

        sdkrecyclerView_offers.setLayoutManager(layoutManager);
        sdkrecyclerView_offers.setAdapter(sdkadapterSdkOffers_offers);

        sdkadapterSdkOffers_offers.setRecyclerClickListener(new AdapterSdkOffers.RecyclerClickListener() {
            @Override
            public void onClick(int position) {


                 modelList = sdkarraymodelLists_offers.get(position);

                if(modelList.getType().equals("tapjoy")){
                   // btnClick_offers.start();

                   // Log.d("TAG","offerrr" + "__" +  tapjoy_id + "__" + tapjoy_placement);
                    OpenTapJoy();
                    return;

                }

                else if(modelList.getType().equals("ironsource")){
                   // btnClick_offers.start();

                    IronSource.showOfferwall(placement_id);
                    return;
                }

                else if(modelList.getType().equals("okspin")){
                  //  btnClick_offers.start();

                    Gamify.openGSpace(place_okspin);
                    return;
                }

                String a = modelList.getType().replace("custom_offers_", "");
                String b = modelList.getType().replace("custom_offers_", "");

               // Toast.makeText(activity, rrr + " " + bbb, Toast.LENGTH_SHORT).show();

                if (a.equals(b)){

                    custom_offers(modelList.getIds_network());
                    // Toast.makeText(activity, rrr, Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

    private void custom_offers(String url_offerwall) {

        String locale = getResources().getConfiguration().locale.getCountry();
        String getIP = getLocalIpAddress();
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String val_0 = url_offerwall.toString();
        String val_1 = val_0.replace("[app_uid]", userid);
        String val_2 = val_1.replace("[app_country]", locale);
        String val_3 = val_2.replace("[app_ip]", getIP);
        String val_4 = val_3.replace("[app_gaid]", androidId);
        String val_final = val_4.toString();


        //Toast.makeText(activity, val_final, Toast.LENGTH_SHORT).show();

        Log.i("TAG","theUrl: "+ val_final);
        Intent url_offer = new Intent(Intent.ACTION_VIEW);
        url_offer.setData(Uri.parse(val_final));
        startActivity(url_offer);




    }

    public void ironofferwall(){

        IronSource.setOfferwallListener(new OfferwallListener() {

            @Override
            public void onOfferwallAvailable(boolean isAvailable) {

            }

            @Override
            public void onOfferwallOpened() {

            }

            @Override
            public void onOfferwallShowFailed(IronSourceError error) {

                Toast.makeText(activity, "There was an error downloading offers, please contact support", Toast.LENGTH_SHORT).show();

            }

            @Override
            public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {

                String final_cridet = String.valueOf(credits);
                reward(final_cridet,"IronSource", transid());

                return true;
            }

            @Override
            public void onGetOfferwallCreditsFailed(IronSourceError error) {

                Toast.makeText(activity, "Coins are not counted from the offer", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOfferwallClosed() {

            }
        });

    }

    //-------offerwalls--------//
    public void SetUpTapJoy(){
        String sdkkey = Constant.getString(activity,IDS.tapjoy_network_id);
        Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
        connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true"); // Disable this in production builds
        Tapjoy.connect(activity,sdkkey, connectFlags, new TJConnectListener() {
            @Override
            public void onConnectSuccess() {
                //this.onConnectSuccess();
                Log.d("TapJoy-Wall","Tapjoyconnected  ");

            }

            @Override
            public void onConnectFailure() {
                // this.onConnectFailure();
                Log.e("TapJoy-Wall","Tapjoy connect error");
            }
        });



    }
    public void OpenTapJoy(){

        Tapjoy.setActivity(activity);


        p_offers = Tapjoy.getPlacement(tapjoy_placement, new TJPlacementListener() {


            @Override
            public void onRequestSuccess(TJPlacement tjPlacement) {
                Log.i("TapJoy-Wall", tjPlacement.toString());
            }

            @Override
            public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
                Log.i("TapJoy-Wall", tjError.message);
            }

            @Override
            public void onContentReady(TJPlacement tjPlacement) {
                Log.i("TapJoy-Wall", tjPlacement.toString());
                p_offers.showContent();
            }

            @Override
            public void onContentShow(TJPlacement tjPlacement) {
                Log.i("TapJoy-Wall", tjPlacement.toString());
            }

            @Override
            public void onContentDismiss(TJPlacement tjPlacement) {
                Log.i("TapJoy-Wall", tjPlacement.toString());

            }

            @Override
            public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {
                Log.i("TapJoy-Wall", tjPlacement.toString());
            }

            @Override
            public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
                Log.i("TapJoy-Wall", tjPlacement.toString()+" "+ tjActionRequest+" "+s+" "+i);
            }

            @Override
            public void onClick(TJPlacement tjPlacement) {
                Log.i("TapJoy-Wall", tjPlacement.toString());
            }


        });

        p_offers.requestContent();

    }
    public void callback_offerwall(){


        // Get notifications whenever Tapjoy currency is earned.
        Tapjoy.setEarnedCurrencyListener(new TJEarnedCurrencyListener() {
            @Override
            public void onEarnedCurrency(String currencyName, int amount) {
                Log.i("TapJoy-Wall", "You've just earned " + amount + " " + currencyName);

                String final_cridet = String.valueOf(amount);
                reward(final_cridet,"TapJoy", transid());

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Tapjoy.getCurrencyBalance(new TJGetCurrencyBalanceListener(){
                    @Override
                    public void onGetCurrencyBalanceResponse(String currencyName, int balance) {
                        Log.i("TapJoy-Wall", "getCurrencyBalance returned " + currencyName + ":" + balance);
                        user_select();
                    }
                    @Override
                    public void onGetCurrencyBalanceResponseFailure(String error) {
                        Log.i("TapJoy-Wall", "getCurrencyBalance error: " + error);

                    }
                });

            }
        },1000);

    }

    // OKSPIN TEMPORARILY DISABLED.
    // The original OkSpin implementation is intentionally kept out of the build
    // until the OkSpin SDK dependency can be resolved. Other offerwalls remain active.
    // To restore OkSpin later, restore this method from the previous project ZIP
    // and restore the com.spin.ok:gp:2.3.4 dependency.

    //-------offerwalls--------//

    public void user_select() {

        // loading();
        Constant.showDialogPageLoading(activity);

        // tv_name.setText("...");
        //  tv_country.setText("...");
         tv_balance_offers.setText("...");

        String userID = Constant.getString(activity,Constant.USER_ID);
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("is_id","is_id");
        params.put("id",userID);

        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"users"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    boolean status = response.getBoolean("status");

                    if (status) {

                        JSONObject object = response.getJSONObject("0");

                        if (object.has("points")) {
                            Constant.setString(activity, Constant.USER_POINTS, object.getString("points"));
                            Log.e("TAG", "onDataChange: " + object.getString("points"));
                            // Toast.makeText(activity, object.getString("points"), Toast.LENGTH_SHORT).show();

                            String  balance = Constant.getString(activity,Constant.USER_POINTS);

                            tv_balance_offers.setText(balance);

                            Constant.hideDialogPageLoading(activity);

                           // Toast.makeText(activity, "Update Balance Successfully!", Toast.LENGTH_SHORT).show();

                        }


                    } else {

                        Constant.showToastMessage(activity, response.getString("message"));

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

    private void reward(final String points,final String reason_type,final String trans_id) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("TAG","device_iddd" + "My ID is: " + androidId);

        String getCountry = getCountryIsoByName.getDeviceCountryCode(activity);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("update_point", "ok");
        params.put("user_id", Constant.getString(activity, Constant.USER_ID));
        params.put("new_point", points);
        params.put("reason", reason_type);
        params.put("date", currentDateandTime);
        params.put("device_id", androidId);
        params.put("trans_id", trans_id);
        params.put("getCountry", getCountry);

        Log.d("TAG", "run: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"updateP"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "response_j " +response.toString());
                try {
                    boolean status = response.getBoolean("status");

                    if (status) {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "statusss: " + response.getString("message"));

                        user_select();

                    } else {

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "statusss: " + response.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    // Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "JSONException: " + e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Log.i("TAG", "onErrorResponse: " + error);
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 30,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public String transid(){
        Random random = new Random();
        int  randomNumber = random.nextInt(1234567890) + 45;
        return String.valueOf(randomNumber);
    }

    public void pup(){

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.drawer_go_trans) {

                Intent tra = new Intent(activity, Transaction_Status.class);
                startActivity(tra);

            }
            if (itemId == R.id.drawer_go_offec) {

                Intent a = new Intent(activity, PrivacyActivity.class);
                a.putExtra("Intent",Constant.getString(this, Constant.i_site_page));
                startActivity(a);
            }
            else if (itemId == R.id.drawer_go_terms) {

                Intent b = new Intent(activity,PrivacyActivity.class);
                b.putExtra("Intent",Constant.getString(this, Constant.i_rules_page));
                startActivity(b);
            }
            else if (itemId == R.id.drawer_go_privacy) {

                Intent c = new Intent(activity,PrivacyActivity.class);
                c.putExtra("Intent",Constant.getString(this, Constant.i_privacy_page));
                startActivity(c);
            }
            else if (itemId == R.id.drawer_go_contact) {

                Intent d = new Intent(activity,PrivacyActivity.class);
                d.putExtra("Intent",Constant.getString(this, Constant.i_contact_page));
                startActivity(d);
            }
            else if (itemId == R.id.drawer_go_exit) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
            return false;
        });
        popupMenu.show();
    }

    public void webbitlabs(){

        String url_bitlabs = "https://web.bitlabs.ai/?uid=" + userid + "&token=" + token_bitlap;
        Log.i("TAG","theUrl: "+ url_bitlabs);
        Intent bitlabs = new Intent(Intent.ACTION_VIEW);
        bitlabs.setData(Uri.parse(url_bitlabs));
        startActivity(bitlabs);

    }


    //session start
    @Override
    protected void onStart() {
        super.onStart();
        Tapjoy.onActivityStart(this);
    }

    //session end
    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(this);

        super.onStop();
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);

        callback_offerwall();
        user_select();

        vpnChecker(activity);
        rootChecker(activity);

    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

}
