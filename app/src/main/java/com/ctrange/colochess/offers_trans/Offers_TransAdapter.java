package com.ctrange.colochess.offers_trans;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrange.colochess.R;

import java.util.ArrayList;

public class Offers_TransAdapter extends RecyclerView.Adapter<Offers_TransAdapter.ViewHolder> {

    // creating a variable for array list and context.
     ArrayList<Offers_TransModule> transModules;
     Context context;

    // creating a constructor for our variables.
    public Offers_TransAdapter(ArrayList<Offers_TransModule> transModules, Context context) {
        this.transModules = transModules;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offers_trans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        Offers_TransModule modal = transModules.get(position);

        holder.date_tv.setText(modal.getT_date_tv());
        holder.type_tv.setText(modal.getT_type_tv());
        holder.trans_id.setText("Trans ID: " + modal.getT_trans_id());
        holder.point_tv.setText(modal.getT_point_tv());

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return transModules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.


          TextView date_tv;
          TextView type_tv;
          TextView trans_id;
          TextView point_tv;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            trans_id = (TextView) itemView.findViewById(R.id.trans_id);
            point_tv = (TextView) itemView.findViewById(R.id.point_tv);


        }
    }




}
