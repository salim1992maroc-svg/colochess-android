package com.ctrange.colochess.sdkoffers;

import static com.ctrange.colochess.App.im;
import static com.ctrange.colochess.App.unt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctrange.colochess.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AdapterSdkOffers extends RecyclerView.Adapter<AdapterSdkOffers.ViewHolder> {

    // creating a variable for array list and context.
    ArrayList<ModelList> sdkModuls;
    Context context;
    RecyclerClickListener recyclerClickListener;

    // creating a constructor for our variables.
    public AdapterSdkOffers(ArrayList<ModelList> sdkModuls, Context context) {
        this.sdkModuls = sdkModuls;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSdkOffers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offerwalls, parent, false);
        return new AdapterSdkOffers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSdkOffers.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        ModelList modal = sdkModuls.get(position);

        holder.tv_title.setText(modal.getName_network());
        holder.tv_desc.setText(modal.getDesc_network());



        Log.i("TAG","hhhhhhhhhh: " + modal.getIds_network());


        Picasso.get().load(unt()+im+"offers/"+modal.getIcon_network()).into(holder.img_logo);

        holder.btn_offers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recyclerClickListener != null) {
                    recyclerClickListener.onClick(holder.getAdapterPosition());

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return sdkModuls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.

        //RelativeLayout itemView1;
        ImageView img_logo;
        TextView tv_title,tv_desc;

        LinearLayout btn_offers;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //itemView1 = (RelativeLayout) itemView.findViewById(R.id.reltv);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);

            btn_offers = (LinearLayout) itemView.findViewById(R.id.btn_offers);


        }
    }

    public void setRecyclerClickListener(AdapterSdkOffers.RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public interface RecyclerClickListener {
        void onClick(int position);
    }


}
