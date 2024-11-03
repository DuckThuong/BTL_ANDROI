package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_andnc_quanlydatdoan.Adapter.OrderHistoryAdapter;
import com.example.btl_andnc_quanlydatdoan.Adapter.Orders;
import com.example.btl_andnc_quanlydatdoan.R;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityOrderHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends BaseActivity {

    private ActivityOrderHistoryBinding binding;
    private OrderHistoryAdapter adapter;
    private DatabaseReference orderHistoryRef;
    private String userId;
   private ArrayList<Orders> ordersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory").child(userId);
        initList();

    }

    private void initList() {
        adapter = new OrderHistoryAdapter(ordersList);

        binding.cardView.setAdapter();
    }
}