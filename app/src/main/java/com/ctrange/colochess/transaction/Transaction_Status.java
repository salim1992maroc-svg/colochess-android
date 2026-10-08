package com.ctrange.colochess.transaction;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ctrange.colochess.App;
import com.ctrange.colochess.R;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Transaction_Status extends AppCompatActivity {

    Activity activity;
    String UserId;

    RecyclerView trans_recyclerView;
    TransAdapter transAdapter;
    ArrayList<TransModule> transModulesList;
    ProgressBar progressBar;

    RequestQueue requestQueue;
    ImageView sign_back_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transaction_status);
         this.activity=Transaction_Status.this;

         UserId = Constant.getString(activity,Constant.USER_ID);

        trans_recyclerView = findViewById(R.id.trans_recyclerview);
        progressBar = findViewById(R.id.transPB);
        transModulesList = new ArrayList<>();
        rewardddd();
        transAdapter = new TransAdapter(transModulesList, activity);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trans_recyclerView.setHasFixedSize(true);
        trans_recyclerView.setLayoutManager(manager);
        trans_recyclerView.setAdapter(transAdapter);


        sign_back_trans = findViewById(R.id.sign_back_trans);

        sign_back_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null) {
                    return;
                }
                onBackPressed();
            }
        });


    }



    private void rewardddd(){

        String tag_json_obj = "json_login_req";
        HashMap<String, String> paramss = new HashMap<String, String>();
        paramss.put("transdata", "ok");
        paramss.put("userid",UserId);


        Log.d("TAG", "run: " + paramss);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"transData"+ex, paramss, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "response_j " +response.toString());
                try {
                    boolean status = response.getBoolean("status");

                    if (status) {

                        progressBar.setVisibility(View.GONE);
                        trans_recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < response.length(); i++) {

                            try {

                                JSONObject responseObj = response.getJSONObject(String.valueOf(i));

                                Log.i("TAG", "json_test" +"|"+ responseObj.toString());

                                String c_trans_status= responseObj.getString("trans_status");
                                String c_username = responseObj.getString("username");
                                String c_email = responseObj.getString("email");
                                String c_date= responseObj.getString("date");
                                String c_character_name = responseObj.getString("character_name");
                                String c_withdrawal = responseObj.getString("withdrawal");
                                String c_transid = responseObj.getString("transid");


                                transModulesList.add(new TransModule(c_trans_status, c_username, c_email, c_date,c_character_name,c_withdrawal,c_transid));



                            } catch (JSONException e) {
                                e.printStackTrace();

                                Log.i("TAG", "json_test" +"|"+ e.getMessage().toString());
                            }
                        }


                    } else {

                        Log.i("TAG", "json_test: " + response.getString("message"));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();

                    // Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "json_test: " + e.getMessage().toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "json_test: " + error.getMessage());
                Log.i("TAG", "json_test: " + error);
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 30,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



}
