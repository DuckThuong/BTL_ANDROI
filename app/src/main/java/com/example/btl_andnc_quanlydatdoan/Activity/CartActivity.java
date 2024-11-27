package com.example.btl_andnc_quanlydatdoan.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.btl_andnc_quanlydatdoan.Adapter.CartAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityCartBinding;
import com.example.btl_andnc_quanlydatdoan.Helper.ManagementCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagementCart(this);

        setUpUI();
        placeOrder();
    }

    private void setUpUI() {
        binding.backBtn.setOnClickListener(v -> finish());
        calculateCart();
        initList();
    }

    private void placeOrder() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getDisplayName() : "Guest";

        binding.placeOrderBtn.setOnClickListener(v -> {
            double totalPrice = managementCart.getTotalPrice();
            String orderId = FirebaseDatabase.getInstance().getReference("Orders").push().getKey();
            ArrayList<Foods> cartItems = managementCart.getListCart();
            int quantity = 0;

            Orders orders = new Orders(orderId, quantity, totalPrice, cartItems);
            Map<String, Object> orderData = prepareOrderData(orders, quantity);

            DatabaseReference orderHistoryRef = FirebaseDatabase.getInstance()
                    .getReference("OrderHistory");
            orderHistoryRef.child(userId).child(orderId).setValue(orderData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            managementCart.clearCart();
                            showToast("Đặt hàng thành công!");
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.scrollViewCart.setVisibility(View.GONE);
                        } else {
                            showToast("Đặt hàng thất bại!");
                        }
                    });
        });
    }

    private Map<String, Object> prepareOrderData(Orders orders, int quantity) {
        Map<String, Object> orderData = new HashMap<>();
        double totalOrderPrice = 0;

        List<Map<String, Object>> foodlist = new ArrayList<>();
        for (Foods food : orders.getListFood()) {
            Map<String, Object> foodData = new HashMap<>();
            foodData.put("FoodId", food.getId());
            foodData.put("Title", food.getTitle());
            foodData.put("Quantity", food.getNumberInCart());
            quantity += food.getNumberInCart();
            foodData.put("Price", food.getPrice());
            foodData.put("TotalPrice", food.getPrice() * food.getNumberInCart());
            foodData.put("ImagePath", food.getImagePath());

            foodlist.add(foodData);
            totalOrderPrice += food.getPrice() * food.getNumberInCart();
        }

        orders.setQuantity(quantity);
        orderData.put("TotalPrice", totalOrderPrice);
        orderData.put("Quantity", quantity);
        orderData.put("Items", foodlist);
        return orderData;
    }

    private void initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        binding.cardView.setAdapter(new CartAdapter(managementCart.getListCart(), this, this::calculateCart));
    }

    @SuppressLint("SetTextI18n")
    private void calculateCart() {
        double delivery = 30;
        double total = managementCart.getTotalFee() + delivery;
        double itemTotal = managementCart.getTotalFee();

        binding.totalFeeTxt.setText(Math.round(itemTotal)+".000 vnd");
        binding.totalTxt.setText(Math.round(total)+".000 vnd");
        binding.deliveryTxt.setText(Math.round(delivery)+".000 vnd");
    }

    private void showToast(String message) {
        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
