package com.example.btl_andnc_quanlydatdoan.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.example.btl_andnc_quanlydatdoan.Helper.ManagementCart;
import com.example.btl_andnc_quanlydatdoan.R;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private Foods object;
    private int num= 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {

        managementCart = new ManagementCart(this);

        binding.backBtn.setOnClickListener(view -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText(Math.round(object.getPrice())+".000 vnd");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar()+" sao");
        binding.ratingBar.setRating((float)object.getStar());
        binding.totalTxt.setText(num*Math.round(object.getPrice())+".000 vnd");

        binding.plusBtn.setOnClickListener(view -> {
            num = num+1;
            binding.numTxt.setText(num+"");
            binding.totalTxt.setText((num*Math.round(object.getPrice()) + ".000 vnd"));
        });

        binding.minusBtn.setOnClickListener(view -> {
            if(num>1)
            {
                num = num-1;
                binding.numTxt.setText(num+"");
                binding.totalTxt.setText((num*Math.round(object.getPrice()) + ".000 vnd"));
            }
        });

        binding.addBtn.setOnClickListener(view -> {
            object.setNumberInCart(num);
            managementCart.insertFood(object);
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}