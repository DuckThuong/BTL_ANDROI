package com.example.btl_andnc_quanlydatdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_andnc_quanlydatdoan.Adapter.BestFoodsAdapter;
import com.example.btl_andnc_quanlydatdoan.Adapter.CategoryAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.Category;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private final ExecutorService executor = Executors.newFixedThreadPool(4); // Thread pool

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUser();
        initBestFood();
        initCategory();
        setVariable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!executor.isShutdown()) {
            executor.shutdown(); // Giải phóng tài nguyên
        }
    }

    private void setVariable() {
        binding.logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        binding.searchBtn.setOnClickListener(view -> {
            String text = binding.searchEdt.getText().toString();
            if (!text.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });

        binding.HistoryBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, OrderHistory.class)));
        binding.cartBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    private void initUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getDisplayName() : "Guest";
        binding.userNameEdt.setText(userId);
    }

    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        ArrayList<Foods> bestFoodList = new ArrayList<>();
        showProgress(binding.progressBarBestFood);

        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                executor.execute(() -> { // Xử lý dữ liệu trên background thread
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            Foods food = issue.getValue(Foods.class);
                            if (food != null) {
                                bestFoodList.add(food);
                            }
                        }
                    }

                    runOnUiThread(() -> { // Cập nhật UI
                        setupBestFoodRecyclerView(bestFoodList);
                        hideProgress(binding.progressBarBestFood);
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                runOnUiThread(() -> {
                    hideProgress(binding.progressBarBestFood);
                    Log.e("Firebase", "Lỗi khi tải BestFood: " + error.getMessage());
                });
            }
        });
    }

    private void setupBestFoodRecyclerView(ArrayList<Foods> list) {
        if (!list.isEmpty()) {
            binding.bestFoodView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            RecyclerView.Adapter adapter = new BestFoodsAdapter(list);
            binding.bestFoodView.setAdapter(adapter);
        }
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        ArrayList<Category> categoryList = new ArrayList<>();
        showProgress(binding.progressBarCategory);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                executor.execute(() -> { // Xử lý dữ liệu trên background thread
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            Category category = issue.getValue(Category.class);
                            if (category != null) {
                                categoryList.add(category);
                            }
                        }
                    }

                    runOnUiThread(() -> { // Cập nhật UI
                        setupCategoryRecyclerView(categoryList);
                        hideProgress(binding.progressBarCategory);
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                runOnUiThread(() -> {
                    hideProgress(binding.progressBarCategory);
                    Log.e("Firebase", "Lỗi tải Category: " + error.getMessage());
                });
            }
        });
    }

    private void setupCategoryRecyclerView(ArrayList<Category> list) {
        if (!list.isEmpty()) {
            binding.categoryView.setLayoutManager(new GridLayoutManager(this, 4));
            RecyclerView.Adapter adapter = new CategoryAdapter(list);
            binding.categoryView.setAdapter(adapter);
        }
    }

    private void showProgress(View progressBar) {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    private void hideProgress(View progressBar) {
        runOnUiThread(() -> progressBar.setVisibility(View.GONE));
    }
}
