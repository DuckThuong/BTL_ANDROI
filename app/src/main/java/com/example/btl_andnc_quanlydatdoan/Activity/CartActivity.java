package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_andnc_quanlydatdoan.Adapter.CartAdapter;
import com.example.btl_andnc_quanlydatdoan.R;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityCartBinding;
import com.example.btl_andnc_quanlydatdoan.Helper.ManagementCart;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managementCart = new ManagementCart(this);

        setVariable();
        calculateCart();
        initList();
    }

    private void initList() {
        if(managementCart.getListCart().isEmpty())
        {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }
        else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);

        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(),this, () -> {
          calculateCart();
        });

        binding.cardView.setAdapter(adapter);
   }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery =10;

        tax = Math.round(managementCart.getTotalFee()*percentTax*100.0);
        double total = Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100)/100;

        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.totalTxt.setText("$"+total);
        binding.deliveryTxt.setText("$"+delivery);
        binding.taxTxt.setText("$"+tax);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }
}