package com.example.btl_andnc_quanlydatdoan.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.btl_andnc_quanlydatdoan.Adapter.UserAdapter;
import com.example.btl_andnc_quanlydatdoan.Domain.User;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends BaseActivity {
    private ActivityUserBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFirebase();
        setRecyclerView();
        setEventListeners();
    }

    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    private void setRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setEventListeners() {
        binding.image.setOnClickListener(v -> openImagePicker());

        binding.btnThem.setOnClickListener(v -> {
            if (isInputValid()) {
                String id = binding.edtId.getText().toString();
                String name = binding.edtName.getText().toString();
                String birth = binding.edtBirth.getText().toString();
                String birthPlace = binding.edtBirthPlace.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String cccd = binding.edtCCCD.getText().toString();

                checkUserExistenceAndUpload(id, name, birth, birthPlace, phone, cccd);
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                setupUserAdapter(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserActivity", "Failed to read value.", error.toException());
            }
        });
    }

    private void setupUserAdapter(List<User> userList) {
        UserAdapter userAdapter = new UserAdapter(userList, new UserAdapter.OnUserClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onUserClick(User user) {
                binding.edtName.setText(user.getName());
                binding.edtBirth.setText(user.getBirth());
                binding.edtBirthPlace.setText(user.getBirthPlace());
                binding.edtCCCD.setText(user.getCccd());
                binding.edtPhone.setText(user.getPhone());
                binding.edtId.setText(user.getId());
                Glide.with(UserActivity.this).load(user.getImageUrl()).into(binding.image);
            }

            @Override
            public void onDeleteClick(String userId) {
                deleteUser(userId);
            }
        });
        binding.recyclerView.setAdapter(userAdapter);
    }

    private void deleteUser(String userId) {
        new AlertDialog.Builder(UserActivity.this)
                .setMessage("Bạn có chắc chắn muốn xóa người dùng này?")
                .setCancelable(false)
                .setPositiveButton("Xóa", (dialog, id) -> {
                    userRef.child(userId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(UserActivity.this, "Người dùng đã được xóa", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(UserActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss())
                .show();
    }

    private boolean isInputValid() {
        String name = binding.edtName.getText().toString();
        String birthPlace = binding.edtBirthPlace.getText().toString();
        String phone = binding.edtPhone.getText().toString();
        String cccd = binding.edtCCCD.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(birthPlace) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cccd)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phone.matches("^\\d{10}$")) {
            Toast.makeText(this, "Số điện thoại phải gồm 10 chữ số!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkUserExistenceAndUpload(String id, String name, String birth, String birthPlace, String phone, String cccd) {
        userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    uploadImageToFirebase(id, birth, birthPlace, name, phone, cccd);
                    Toast.makeText(UserActivity.this, "Cập nhật người dùng thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageToFirebase(id, birth, birthPlace, name, phone, cccd);
                    Toast.makeText(UserActivity.this, "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserActivity", "Error checking user existence", error.toException());
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            binding.image.setImageURI(selectedImageUri); // Hiển thị ảnh đã chọn
        }
    }

    private void uploadImageToFirebase(String id, String birth, String birthPlace, String name, String phone, String cccd) {
        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveUserData(id, birth, birthPlace, name, phone, cccd, imageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(UserActivity.this, "Tải ảnh lên thất bại!", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveUserData(String id, String birth, String birthPlace, String name, String phone, String cccd, String imageUrl) {
        User user = new User(id, birth, birthPlace, name, phone, cccd, imageUrl);
        userRef.child(id).setValue(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show());
    }
}
