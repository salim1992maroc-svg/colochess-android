package com.ctrange.colochess.transaction;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrange.colochess.R;


import java.util.ArrayList;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder> {

    // creating a variable for array list and context.
     ArrayList<TransModule> transModules;
     Context context;

    // creating a constructor for our variables.
    public TransAdapter(ArrayList<TransModule> transModules, Context context) {
        this.transModules = transModules;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        TransModule modal = transModules.get(position);

        String startuss = modal.getTv_trans_status();

        if(startuss.equals("0")){
            holder.tvv_trans_status.setText("Completed successful");
            holder.tvv_trans_status.setBackgroundColor(Color.parseColor("#09CC11"));
            holder.tvv_trans_status.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(startuss.equals("1")){
            holder.tvv_trans_status.setText("Processing...");
            holder.tvv_trans_status.setBackgroundColor(Color.parseColor("#FFC107"));
            holder.tvv_trans_status.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if(startuss.equals("2")){
            holder.tvv_trans_status.setText("Rejected");
            holder.tvv_trans_status.setBackgroundColor(Color.parseColor("#FF0000"));
            holder.tvv_trans_status.setTextColor(Color.parseColor("#FFFFFF"));
        }


        holder.tvv_username.setText(modal.getTv_username());
        holder.tvv_email.setText(modal.getTv_email());

        holder.tvv_date.setText(modal.getTv_date());
        holder.tvv_character.setText(modal.getTv_character());
        holder.tvv_withdrawal.setText(modal.getTv_withdrawal());

        holder.tvv_transid.setText(modal.getTv_transid());

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return transModules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.

          LinearLayout liner_main;

          TextView tvv_trans_status;
          TextView tvv_username;
          TextView tvv_email;
          TextView tvv_date;
          TextView tvv_character;
          TextView tvv_withdrawal;
          TextView tvv_transid;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            liner_main = itemView.findViewById(R.id.liner_main);

            tvv_trans_status = (TextView) itemView.findViewById(R.id.tv_trans_status);
            tvv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tvv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tvv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tvv_character = (TextView) itemView.findViewById(R.id.tv_character);
            tvv_withdrawal = (TextView) itemView.findViewById(R.id.tv_withdrawal);
            tvv_transid = (TextView) itemView.findViewById(R.id.tv_transid);

        }
    }




}
