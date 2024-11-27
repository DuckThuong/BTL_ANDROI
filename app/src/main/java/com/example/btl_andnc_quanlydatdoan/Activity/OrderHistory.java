package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.btl_andnc_quanlydatdoan.Adapter.OrderHistoryAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityOrderHistoryBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends BaseActivity {

    private ActivityOrderHistoryBinding binding;
    private OrderHistoryAdapter adapter;
    private DatabaseReference orderHistoryRef;
   private final ArrayList<Orders> ordersList = new ArrayList<>();
   private ArrayList<Foods> foodList = new ArrayList<>();
   GenericTypeIndicator<ArrayList<Foods>> t = new GenericTypeIndicator<ArrayList<Foods>>() {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUser();
        setVariable();
        initList();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userName = currentUser != null ? currentUser.getDisplayName() : "Guest";
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory").child(userName);
    }

    private void initList() {
        adapter = new OrderHistoryAdapter(ordersList);

        binding.cardView.setLayoutManager(new LinearLayoutManager(this));
        binding.cardView.setAdapter(adapter);
        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                    String orderId = orderSnapshot.getKey();
                    int totalPrice = orderSnapshot.child("TotalPrice").getValue(Integer.class);
                    int quantity = orderSnapshot.child("Quantity").getValue(int.class);
                    String orderStatus = orderSnapshot.child("Status").getValue(String.class);
                    foodList = orderSnapshot.child("Items").getValue(t);

                    Orders order = new Orders(orderId,quantity , totalPrice, foodList, orderStatus);
                    ordersList.add(order);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}