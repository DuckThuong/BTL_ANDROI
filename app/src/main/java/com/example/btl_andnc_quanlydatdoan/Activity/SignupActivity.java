package com.example.btl_andnc_quanlydatdoan.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_andnc_quanlydatdoan.R;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends BaseActivity {
ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivitySignupBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       setVariable();
    }

    private void setVariable() {

        binding.SignupBtn.setOnClickListener(view -> {

            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();
            String username = binding.userNameEdt.getText().toString();

            if(password.length()<6)
            {
                Toast.makeText(SignupActivity.this, "Mật khẩu phải lớn hơn 6 kí tự", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, task -> {
                if(task.isComplete())
                {
                    FirebaseUser user = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build();

                    user.updateProfile(profileChangeRequest);

                    Log.i(TAG, "onComplete: ");
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(SignupActivity.this, "Thất bại" + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}