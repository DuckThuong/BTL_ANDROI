package com.example.btl_andnc_quanlydatdoan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
import com.example.btl_andnc_quanlydatdoan.R;

import java.util.ArrayList;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.viewholder> {
    ArrayList<Foods> items;
    Orders order;
    Context context;

    public DetailHistoryAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DetailHistoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_detail_history,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryAdapter.viewholder holder, int position) {
        Foods foods = items.get(position);
        holder.titleTxt.setText(foods.getTitle());
        holder.totalPriceTxt.setText(Math.round(foods.getPrice()) + ".000 vnd");

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        TextView titleTxt, quantityTxt, totalPriceTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
        }
    }
}
