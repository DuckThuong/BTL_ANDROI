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
import com.example.btl_andnc_quanlydatdoan.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.viewholder> {
    ArrayList<Orders> orderList;
    Context context;

    public OrderHistoryAdapter(ArrayList<Orders> items)
    {
        this.orderList = items;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_history,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.viewholder holder, int position) {
        Orders order = orderList.get(position);
        holder.orderId.setText(order.getId());
        holder.orderDate.setText(order.getDate().getDay());
        holder.orderTime.setText(order.getDate().getHours());
        holder.totalPrice.setText(Math.round(order.getTotalPrice())+ ".000 vnd");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView orderId, orderDate, totalPrice, orderTime;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
