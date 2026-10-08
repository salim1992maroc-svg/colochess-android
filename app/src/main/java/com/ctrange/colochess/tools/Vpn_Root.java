package com.ctrange.colochess.tools;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import com.airbnb.lottie.LottieAnimationView;
import com.scottyab.rootbeer.RootBeer;
import com.ctrange.colochess.R;


public class Vpn_Root {

    @SuppressLint("StaticFieldLeak")
    public static TextView title_tv_dialog,desc_tv_dialog;
    public static AppCompatButton vpn_btn,root_btn,multiple_btn;
    public static LottieAnimationView lottie;

    public static void vpnChecker(Activity context){

        String VPNController = Constant.getString(context,Constant.co_vpn);

        //code vpnChecker
        if(VPNController.equals("on")){

            ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                Network activeNet = cManager.getActiveNetwork();
                NetworkCapabilities netCaps =cManager.getNetworkCapabilities(activeNet);
                boolean vpnConnection = netCaps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);

                if(vpnConnection){

                    showVPNDialog(context);

                }

            }

        }

    }

    public static void showVPNDialog(Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vpn_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        vpn_btn = dialog.findViewById(R.id.vpn_btn);
        ImageView exite1 = dialog.findViewById(R.id.exite1);

        exite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        vpn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.show();
    }

    public static void rootChecker(Activity context){

        String ROOTController = Constant.getString(context,Constant.co_root);

      if(ROOTController.equals("on")) {

          RootBeer rootBeer = new RootBeer(context);

          if (rootBeer.isRooted()) {

              showRootDialog(context);
              // Toast.makeText(context, "rooted", Toast.LENGTH_SHORT).show();

          }

      }

    }

    public static void showRootDialog(Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.root_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        root_btn = dialog.findViewById(R.id.root_btn);

        ImageView exite2 = dialog.findViewById(R.id.exite2);

        exite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        root_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.show();
    }

    public static void showmultipleDialog(Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.multiple_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        multiple_btn = dialog.findViewById(R.id.multiple_btn);

        ImageView exite2 = dialog.findViewById(R.id.exite2);

        exite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        multiple_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        dialog.show();
    }


}
