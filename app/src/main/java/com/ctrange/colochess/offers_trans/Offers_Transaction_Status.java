package com.ctrange.colochess.offers_trans;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class Offers_Transaction_Status extends AppCompatActivity {

    Activity activity;
    String UserId;

    RecyclerView offers_trans_recyclerView;
    Offers_TransAdapter offers_transAdapter;
    ArrayList<Offers_TransModule> offers_transModulesList;
    ProgressBar offers_progressBar;

    RequestQueue offers_requestQueue;
    ImageView offers_sign_back_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.offers_transaction_status);
         this.activity= Offers_Transaction_Status.this;

         UserId = Constant.getString(activity,Constant.USER_ID);

        offers_trans_recyclerView = findViewById(R.id.offers_trans_recyclerview);
        offers_progressBar = findViewById(R.id.offers_transPB);
        offers_transModulesList = new ArrayList<>();
        rewardddd();
        offers_transAdapter = new Offers_TransAdapter(offers_transModulesList, activity);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        offers_trans_recyclerView.setHasFixedSize(true);
        offers_trans_recyclerView.setLayoutManager(manager);
        offers_trans_recyclerView.setAdapter(offers_transAdapter);


        offers_sign_back_trans = findViewById(R.id.offers_sign_back_trans);
        offers_sign_back_trans.setOnClickListener(new View.OnClickListener() {
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
                unt()+ap+"offerstrans"+ex, paramss, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "response_j " +response.toString());
                try {
                    boolean status = response.getBoolean("status");

                    if (status) {

                        offers_progressBar.setVisibility(View.GONE);
                        offers_trans_recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < response.length(); i++) {

                            try {

                                JSONObject responseObj = response.getJSONObject(String.valueOf(i));

                                Log.i("TAG", "json_test" +"|"+ responseObj.toString());

                                String c_date = responseObj.getString("date");
                                String c_type = responseObj.getString("type");
                                String c_trans_id = responseObj.getString("trans_id");
                                String c_points = responseObj.getString("points");



                                offers_transModulesList.add(new Offers_TransModule(c_date, c_type, c_trans_id, c_points));



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
