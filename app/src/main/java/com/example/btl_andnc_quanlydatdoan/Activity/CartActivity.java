package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_andnc_quanlydatdoan.Adapter.CartAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.Orders;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityCartBinding;
import com.example.btl_andnc_quanlydatdoan.Helper.ManagementCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity{

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
        placeOrder();

    }

    private void placeOrder()
    {
        binding.placeOrderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double totalPrice = managementCart.getTotalPrice();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String orderId = FirebaseDatabase.getInstance().getReference("Orders").push().getKey();
                ArrayList<Foods> cartItems = managementCart.getListCart();
                int quantity=0;

                Orders orders = new Orders(orderId,quantity, totalPrice, cartItems);

                Map<String,Object> orderData = new HashMap<>();
                double totalOrderPrice = 0;

                List<Map<String,Object>> foodlist = new ArrayList<>();
                for(Foods food: orders.getListFood()){
                    Map<String, Object> foodData = new HashMap<>();
                    foodData.put("FoodId", food.getId());
                    foodData.put("Title", food.getTitle());
                    foodData.put("Quantity", food.getNumberInCart());
                    quantity += food.getNumberInCart();
                    orders.setQuantity(quantity);
                    foodData.put("Price", food.getPrice());
                    foodData.put("TotalPrice",food.getPrice()*food.getNumberInCart());
                    foodData.put("ImagePath", food.getImagePath());

                    foodlist.add(foodData);
                    totalOrderPrice += food.getPrice()*food.getNumberInCart();
                }

                orderData.put("TotalPrice", totalOrderPrice);
                orderData.put("Quantity", quantity);
                orderData.put("Items",foodlist);

                DatabaseReference orderHistoryRef = FirebaseDatabase.getInstance()
                        .getReference("OrderHistory");
                orderHistoryRef.child(userId).child(orderId).setValue(orderData)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                managementCart.clearCart();
                                Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT ).show();
                                binding.emptyTxt.setVisibility(View.VISIBLE);
                                binding.scrollViewCart.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(CartActivity.this, "Dặt hàng thất bại!", Toast.LENGTH_SHORT ).show();
                            }
                        } );
            }
        });
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