package com.ctrange.colochess.acount;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ctrange.colochess.R;
import com.ctrange.colochess.SplashActivity;
import com.ctrange.colochess.tools.Constant;


public class Accept_Conditions extends AppCompatActivity {

    Activity activity;
    String getLik;
    TextView btn_rulls;
    private SwipeRefreshLayout swipeRefreshLayoutAccept;
    WebView webview_rulls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.accept_conditions);
        activity=this;

        btn_rulls = findViewById(R.id.select_btn);
        swipeRefreshLayoutAccept = findViewById(R.id.swipeRefreshLayout);
        webview_rulls = findViewById(R.id.webview_rulls);

        if (Constant.isNetworkAvailable(activity)) {

            onClick();

        } else {

            Constant.InternetErrorDialog(activity);
        }


    }

    private void onClick() {

        getLik = Constant.getString(activity,Constant.i_rules_page);


        btn_rulls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(activity, SplashActivity.class));
                finish();
            }
        });


        swipeRefreshLayoutAccept.post(new Runnable() {
            @Override
            public void run() {

                getLik = Constant.getString(activity,Constant.i_rules_page);
                swipeRefreshLayoutAccept.setRefreshing(true);

                if(getLik.equals("")) {
                    onClick();
                }else{
                    LoadPage();
                }
            }
        });

        swipeRefreshLayoutAccept.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLik = Constant.getString(activity,Constant.i_rules_page);
                if(getLik.equals("")) {
                    onClick();
                }else{
                    LoadPage();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage() {

        webview_rulls.setWebViewClient(new Accept_Conditions.MyWebViewClient());
        webview_rulls.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayoutAccept.setRefreshing(false);
                } else {
                    swipeRefreshLayoutAccept.setRefreshing(true);
                }
            }
        });

        webview_rulls.getSettings().setLoadsImagesAutomatically(true);
        webview_rulls.getSettings().setJavaScriptEnabled(true);
        webview_rulls.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview_rulls.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview_rulls.getSettings().setDomStorageEnabled(true);
        webview_rulls.getSettings().setDatabaseEnabled(true);
        webview_rulls.getSettings().setGeolocationEnabled(true);
        webview_rulls.loadUrl(getLik);

        webview_rulls.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String getLik, Bitmap favicon) {
                super.onPageStarted(view, getLik, favicon);

            }

            @Override
            public void onPageFinished(WebView view,String getLik) {

                CookieSyncManager.createInstance(webview_rulls.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                //cookieManager.removeSessionCookie();

                cookieManager.setCookie(getLik,"cookies-state=accepted");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                } else {
                    CookieSyncManager.getInstance().sync();
                }


                super.onPageFinished(view,getLik);
            }

        });


    }


    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String getLik) {
            view.loadUrl(getLik);
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
