package com.example.btl_andnc_quanlydatdoan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.btl_andnc_quanlydatdoan.Activity.DetailHistory;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
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
        holder.quantityTxt.setText("Tổng sản phẩm: " +order.getQuantity());
        holder.totalPrice.setText(Math.round(order.getTotalPrice())+ ".000 vnd");
        holder.titleTxt.setText(order.getListFood().get(0).getTitle());
        String imagePath = order.getListFood().get(0).getImagePath();

        Glide.with(holder.itemView.getContext())
                .load(imagePath)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailHistory.class);
                intent.putExtra("Items",order.getListFood());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailHistory.class);
                intent.putExtra("Items",order.getListFood());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView orderId, totalPrice, titleTxt, quantityTxt;
        Button btn;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            btn = itemView.findViewById(R.id.button2);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
