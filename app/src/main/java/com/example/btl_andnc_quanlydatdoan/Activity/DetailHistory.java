package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.btl_andnc_quanlydatdoan.Adapter.DetailHistoryAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityDetailHistoryBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailHistory extends BaseActivity {

    private ArrayList<Foods> foodsList = new ArrayList<>();
    private ArrayList<Orders> orderList = new ArrayList<>();
    private ActivityDetailHistoryBinding binding;
    private DetailHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        initList();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initList() {

            foodsList = (ArrayList<Foods>) getIntent().getSerializableExtra("Items");
            //orderList = (ArrayList<Orders>) getIntent().getSerializableExtra("Orders");
            adapter = new DetailHistoryAdapter(foodsList);

            binding.cardView.setLayoutManager(new LinearLayoutManager(this));
            binding.cardView.setAdapter(adapter);
    }
}