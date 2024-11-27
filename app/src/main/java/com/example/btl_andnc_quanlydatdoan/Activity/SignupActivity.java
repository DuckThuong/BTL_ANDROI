package com.example.btl_andnc_quanlydatdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.btl_andnc_quanlydatdoan.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
    }

    private void initUI() {
        binding.SignupBtn.setOnClickListener(view -> handleSignup());
        binding.loginBtn.setOnClickListener(view -> navigateToLogin());
    }

    private void handleSignup() {
        String email = binding.userEdt.getText().toString().trim();
        String password = binding.passEdt.getText().toString().trim();
        String username = binding.userNameEdt.getText().toString().trim();

        // Kiểm tra đầu vào
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải lớn hơn 6 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký tài khoản với Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUserProfile(username);
                    } else {
                        handleSignupError(task.getException());
                    }
                });
    }

    private void updateUserProfile(String username) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Log.e(TAG, "Lỗi: FirebaseUser null sau khi đăng ký");
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật tên người dùng
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Cập nhật tên người dùng thành công");
                        navigateToLogin();
                    } else {
                        Log.e(TAG, "Cập nhật tên người dùng thất bại: " + task.getException());
                        Toast.makeText(this, "Không thể cập nhật thông tin người dùng!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSignupError(Exception exception) {
        String errorMessage = exception != null ? exception.getMessage() : "Lỗi không xác định";
        Log.e(TAG, "Đăng ký thất bại: " + errorMessage);
        Toast.makeText(this, "Đăng ký thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}
