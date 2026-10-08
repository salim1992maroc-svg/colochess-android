package com.ctrange.colochess.characters;

import static com.ctrange.colochess.App.im;
import static com.ctrange.colochess.App.unt;

import android.content.Context;
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

public class PayeerAdapter extends RecyclerView.Adapter<PayeerAdapter.ViewHolder> {

    // creating a variable for array list and context.
     ArrayList<PayeerModal> reddemModals;
     Context context;
     RecyclerClickListener recyclerClickListener;

    // creating a constructor for our variables.
    public PayeerAdapter(ArrayList<PayeerModal> reddemModals, Context context) {
        this.reddemModals = reddemModals;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        PayeerModal modal = reddemModals.get(position);

        holder.tv_payment_title.setText(modal.getModel_dollar_value());
        holder.tv_minimumredeem.setText(modal.getModel_minimum_redeem());
        holder.tv_payment_desc.setText(modal.getModel_discreption_redeem());

        Picasso.get().load(unt()+im+modal.getModel_image_url()).into(holder.image_payment);


        holder.card_click.setOnClickListener(new View.OnClickListener() {

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
        return reddemModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.

          LinearLayout card_click;

          TextView tv_payment_title;
          TextView tv_minimumredeem;
          TextView tv_payment_desc;

          ImageView image_payment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_click = (LinearLayout) itemView.findViewById(R.id.card_click);

            tv_payment_title = (TextView) itemView.findViewById(R.id.tv_payment_title);
            tv_payment_desc = (TextView) itemView.findViewById(R.id.tv_payment_desc);
            image_payment = (ImageView) itemView.findViewById(R.id.image_payment);
            tv_minimumredeem = (TextView) itemView.findViewById(R.id.tv_minimumredeem);


        }
    }

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public interface RecyclerClickListener {
        void onClick(int position);
    }


}
