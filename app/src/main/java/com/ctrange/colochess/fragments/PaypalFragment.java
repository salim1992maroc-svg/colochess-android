package com.ctrange.colochess.fragments;

import static com.ctrange.colochess.App.ap;
import static com.ctrange.colochess.App.ex;
import static com.ctrange.colochess.App.unt;
import static com.ctrange.colochess.tools.Constant.hideKeyboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
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
import com.ctrange.colochess.R;
import com.ctrange.colochess.characters.PaypalAdapter;
import com.ctrange.colochess.characters.PaypalModal;
import com.ctrange.colochess.tools.Constant;
import com.ctrange.colochess.tools.CustomVolleyJsonRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaypalFragment extends Fragment {

    private static final String TAG = "Page2";

    private ProgressBar progressBar_paypal;

    private RecyclerView recyclerView_paypal;
    private PaypalAdapter adapter_paypal;
    private ArrayList<PaypalModal> paypalModal = new ArrayList<>();


    SweetAlertDialog paypalpDialog;
    TextView tv_name,tv_balance;
    String user_name;
    ImageView refresh;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paypallfragment, container, false);

        tv_name = view.findViewById(R.id.tv_name_paypal);
        tv_balance = view.findViewById(R.id.tv_balance_paypal);
        refresh = view.findViewById(R.id.btn_refresh_paypal);

        user_name = Constant.getString(getActivity(),Constant.USER_NAME);

        tv_name.setText(user_name);

        progressBar_paypal = view.findViewById(R.id.progressBar_paypal);

        recyclerView_paypal = view.findViewById(R.id.recyclerview_paypal);
        progressBar_paypal = view.findViewById(R.id.progressBar_paypal);

        RecyclerView.LayoutManager layoutManager = null;
        adapter_paypal = new PaypalAdapter(paypalModal, getActivity());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL/* orientation*/);

        // recyclerView.setHasFixedSize(true);
        recyclerView_paypal.setLayoutManager(layoutManager);
        recyclerView_paypal.setAdapter(adapter_paypal);

        getData();

        adapter_paypal.setRecyclerClickListener(new PaypalAdapter.RecyclerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(int position) {

                PaypalModal rdm = paypalModal.get(position);

                String min =  rdm.getModel_minimum_redeem();
                String payment = rdm.getModel_payment_methods();
                String balance = Constant.getString(getActivity(),Constant.USER_POINTS);

                int bal = 0;
                int  mim = Integer.parseInt(min);

                try {
                    bal = Integer.parseInt(balance);
                } catch(NumberFormatException nfe) {
                    Log.i("TAG" ,"" + nfe);
                }

                if(bal >= mim){

                    shownotifymessage(payment,min);

                }else{

                    dialog_no_coins();

                }

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_select();

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getData() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                unt()+ap+"paypal"+ex, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar_paypal.setVisibility(View.GONE);
                recyclerView_paypal.setVisibility(View.VISIBLE);

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject responseObj = response.getJSONObject(i);
                        String c_dollar_value = responseObj.getString("dollar_value");
                        String c_cara_name = responseObj.getString("type");
                        String c_cara_desc = responseObj.getString("cara_desc");
                        String c_cara_image = responseObj.getString("cara_image");
                        String c_cara_points = responseObj.getString("cara_points");

                        paypalModal.add(new PaypalModal(c_dollar_value,c_cara_name, c_cara_desc,c_cara_image,c_cara_points));

                        Log.e("TAG", "json_test" +"|"+ c_cara_name +"|"+ c_cara_desc +"|"+ c_cara_image +"|"+ c_cara_points);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(getActivity(), "JSONException:|" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "VolleyError:|" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }


    public void dialog_no_coins() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nocoins);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        AppCompatButton btn_noCoins = dialog.findViewById(R.id.btn_noCoins);

        btn_noCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void shownotifymessage(String name_character,String minn) {

        String username = Constant.getString(getActivity(),Constant.USER_NAME);
        String eml = Constant.getString(getActivity(),Constant.USER_EMAIL);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notify);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextInputEditText edite_name = dialog.findViewById(R.id.edite_name);
        TextInputEditText edite_email = dialog.findViewById(R.id.edite_email);
        EditText edite_message = dialog.findViewById(R.id.edite_message);
        Button btn_send = dialog.findViewById(R.id.btn_send);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        edite_name.setText(username);
        edite_email.setText(eml);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name_ = edite_name.getText().toString();
                String Email_ = edite_email.getText().toString();
                String Message_ = edite_message.getText().toString();

                if (Name_.length() == 0) {
                    edite_name.setError(getResources().getString(R.string.enter_name));
                    edite_name.requestFocus();
                } else if (Email_.length() == 0) {
                    edite_email.setError(getResources().getString(R.string.enter_email));
                    edite_email.requestFocus();
                } else if (!Constant.isValidEmailAddress(Email_)) {
                    edite_email.setError(getResources().getString(R.string.enter_valid_email));
                    edite_email.requestFocus();
                } if (Message_.length() == 0) {
                    edite_message.setError(getResources().getString(R.string.enter_message));
                    edite_message.requestFocus();
                }else {
                    hideKeyboard(getActivity());
                    loading();

                    senderNotify(Name_,Email_,Message_,name_character,minn);

                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void loading(){

        paypalpDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        paypalpDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        paypalpDialog.setTitleText("Loading ...");
        paypalpDialog.setCancelable(true);
        paypalpDialog.show();

    }
    public void senderNotify(String name,String email,String message,String name_character,String mmin){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String device_name = android.os.Build.MODEL;
        String device_brand = Build.BRAND;

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("is_notify", "ok");
        params.put("user_id", Constant.getString(App.getContext(), Constant.USER_ID));
        params.put("t_name", name);
        params.put("t_email", email);
        params.put("t_name_character", name_character);
        params.put("t_message", message);
        params.put("t_min", mmin);
        params.put("t_date", currentDateandTime);
        params.put("t_transid", transid());
        params.put("t_device_name", device_name);
        params.put("t_device_brand", device_brand);

        Log.d("TAG", "run: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                unt()+ap+"notify"+ex, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "response_j " +response.toString());
                try {
                    boolean status = response.getBoolean("status");

                    if (status) {

                        paypalpDialog.dismissWithAnimation();
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "statusss: " + response.getString("message"));

                         user_select();

                    } else {
                        paypalpDialog.dismissWithAnimation();
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "statusss: " + response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    paypalpDialog.dismissWithAnimation();
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "JSONException: " + e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                paypalpDialog.dismissWithAnimation();
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

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        user_select();
    }

    public void user_select() {

        loading();
        //Constant.showDialogPageLoading(getActivity());

        tv_balance.setText("...");

        String userID = Constant.getString(getActivity(),Constant.USER_ID);
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
                            Constant.setString(getActivity(), Constant.USER_POINTS, object.getString("points"));
                            Log.e("TAG", "onDataChange: " + object.getString("points"));

                            String  balance = Constant.getString(getActivity(),Constant.USER_POINTS);

                            tv_balance.setText(balance);

                            // Constant.hideDialogPageLoading(getActivity());

                            // Toast.makeText(activity, "Update Balance Successfully!", Toast.LENGTH_SHORT).show();
                            paypalpDialog.dismissWithAnimation();
                        }

                    } else {

                        Constant.showToastMessage(getActivity(), response.getString("message"));
                        paypalpDialog.dismissWithAnimation();
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
                paypalpDialog.dismissWithAnimation();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(getActivity(), getActivity().getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}