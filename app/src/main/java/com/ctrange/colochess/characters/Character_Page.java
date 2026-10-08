package com.ctrange.colochess.characters;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ctrange.colochess.PrivacyActivity;
import com.ctrange.colochess.fragments.PaeyerFragment;
import com.ctrange.colochess.fragments.PaypalFragment;
import com.ctrange.colochess.transaction.Transaction_Status;
import com.google.android.material.tabs.TabLayout;
import com.ctrange.colochess.R;
import com.ctrange.colochess.tools.Constant;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class Character_Page extends AppCompatActivity {

    int delay;
    String place;
    private Character_Page activity;



    public static MediaPlayer btnClick,fatal_errer;

    //for character list

    ProgressBar progressBar_cara;
    PopupMenu popupMenu;
    ImageView popupMenu_cara;
    Thread thread;

    TabLayout mTabs;
    View mIndicator;
    ViewPager mViewPager;
    private int indicatorWidth;

    SweetAlertDialog pDialog;

    Button button;


    ImageView sign_back_cara;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_character_page);
        activity=this;
        btnClick = MediaPlayer.create(activity,R.raw.click);
        fatal_errer = MediaPlayer.create(activity,R.raw.fatal_error);

        //PageView Start
        //Assign view reference
        mTabs = findViewById(R.id.tab);
        mIndicator = findViewById(R.id.indicator);
        mViewPager = findViewById(R.id.viewPager);

        String t_payyer = Constant.getString(activity,Constant.tab_payeer);
        String t_payypal = Constant.getString(activity,Constant.tab_paypal);

        //Set up the view pager and fragments
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new PaeyerFragment(), t_payyer);
        adapter.addFragment(new PaypalFragment(), t_payypal);
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);

        //Determine indicator width at runtime
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+i) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //PageView End

        sign_back_cara = findViewById(R.id.sign_back_cara);
        sign_back_cara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });




        popupMenu_cara = findViewById(R.id.popupMenu_cara);

        popupMenu = new PopupMenu(activity, popupMenu_cara);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setForceShowIcon(true);




        popupMenu_cara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pup();

            }
        });



    }

    public void loading(){

        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

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

    @Override
    protected void onResume() {
        super.onResume();



    }


}