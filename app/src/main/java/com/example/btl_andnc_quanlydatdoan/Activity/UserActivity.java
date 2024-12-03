package com.example.btl_andnc_quanlydatdoan.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    ActivityUserBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private UserAdapter userAdapter;
    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.image.setOnClickListener(v -> {
            openImagePicker();
        });


        binding.btnThem.setOnClickListener(view -> {
            String name = binding.edtName.getText().toString();
            String birth = binding.edtBirth.getText().toString();
            String birthPlace = binding.edtBirthPlace.getText().toString();
            String cccd = (binding.edtCCCD.getText().toString());
            String phone = (binding.edtPhone.getText().toString());
            String id = binding.edtId.getText().toString();

            if (name.isEmpty() || birthPlace.isEmpty() || phone == null || cccd == null)  {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (!phone.toString().matches("^\\d{10}$")) {
                Toast.makeText(this, "Số điện thoại phải gồm 9 chữ số!", Toast.LENGTH_SHORT).show();
            } else if (selectedImageUri == null){
                Toast.makeText(this, "Vui lòng chọn ảnh đại diện",Toast.LENGTH_SHORT).show();
            }else {
                if (id != null && !id.isEmpty()) {
                    // Kiểm tra nếu `id` đã tồn tại trong cơ sở dữ liệu Firebase
                    userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Nếu ID đã tồn tại, cập nhật thông tin người dùng
                                uploadImageToFirebase(id, birth, birthPlace, name, phone, cccd, selectedImageUri);
                                Toast.makeText(UserActivity.this, "Cập nhật người dùng thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Nếu ID chưa tồn tại, tạo người dùng mới
                                uploadImageToFirebase(id, birth, birthPlace, name, phone, cccd, selectedImageUri);
                                Toast.makeText(UserActivity.this, "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
                                //finish();  // Kết thúc Activity sau khi thêm thành công
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI17n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    //Log.d("UserActivity", "Found userId: " + userId);
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userAdapter = new UserAdapter(userList, new UserAdapter.OnUserClickListener() {

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
                if (userId == null || userId.isEmpty()) {
                   // Log.e("DeleteUser", "userId không hợp lệ hoặc null");
                    Toast.makeText(UserActivity.this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Failed to read value.", error.toException());
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.image.setImageURI(selectedImageUri); // Hiển thị ảnh đã chọn
        }
    }
    private void uploadImageToFirebase(String id, String birth, String birthPlace, String name, String phone, String cccd, Uri imageUri) {
        // Lấy đường dẫn tới thư mục Firebase Storage
        StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveUserData(id, birth,  birthPlace,  name, phone, cccd, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserActivity.this, "Tải ảnh lên thất bại!", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserData(String id, String birth, String birthPlace, String name, String phone, String cccd, String imageUrl) {
        User user = new User(id, birth, birthPlace, name, phone, cccd, imageUrl);

        userRef.child(id).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                });
    }

}