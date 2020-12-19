package com.epayeats.epayeatsdeliverypartner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsdeliverypartner.Model.orderModel;
import com.epayeats.epayeatsdeliverypartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DelivredOrderAdapter extends RecyclerView.Adapter<DelivredOrderAdapter.ImageHolder>
{
    Context context;
    List<orderModel> model;
    OnitemClickListener mlistener;

    public DelivredOrderAdapter(Context context, List<orderModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.deliverd_view, parent, false);
        return new ImageHolder(view);

    }
    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView current_name, current_price, current_qty, current_date, current_username, current_mobile, ordered_date, ordered_time, delivered_time;
        ImageView image;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            current_name = itemView.findViewById(R.id.delivered_name);
            current_price = itemView.findViewById(R.id.delivered_price);
            current_qty = itemView.findViewById(R.id.delivered_qty);
            image = itemView.findViewById(R.id.delivered_image);
            current_date = itemView.findViewById(R.id.delivered_date);
            current_username = itemView.findViewById(R.id.delivered_username);
            current_mobile = itemView.findViewById(R.id.delivered_mobile);
            ordered_date = itemView.findViewById(R.id.ordered_date);
            ordered_time = itemView.findViewById(R.id.ordered_time);
            delivered_time = itemView.findViewById(R.id.delivered_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mlistener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mlistener.onItemClick(position);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {

        orderModel currentUpload = model.get(position);

        holder.current_name.setText(currentUpload.getMenuName());
        holder.current_price.setText(currentUpload.getOfferPrice());
        holder.current_qty.setText(currentUpload.getQty());
        holder.current_date.setText(currentUpload.getDeliveryDate());
        holder.current_username.setText(currentUpload.getcName());
        holder.current_mobile.setText(currentUpload.getcPhone());
        holder.ordered_date.setText(currentUpload.getOrderDate());
        holder.ordered_time.setText(currentUpload.getOrderTime());
        holder.delivered_time.setText(currentUpload.getDeliveryTime());
        Picasso.get()
                .load(currentUpload.getMenuImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.image);
    }
    public interface OnitemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(OnitemClickListener listener)
    {
        mlistener = listener;
    }
}

