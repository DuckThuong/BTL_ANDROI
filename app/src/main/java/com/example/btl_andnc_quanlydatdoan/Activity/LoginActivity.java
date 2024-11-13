package com.example.btl_andnc_quanlydatdoan.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_andnc_quanlydatdoan.R;
import com.example.btl_andnc_quanlydatdoan.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, options);


        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleSignInClient.signOut().addOnCompleteListener(task -> {
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, 11);
                });

                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 11);
            }
        });
        setVariable();
    }

    private void setVariable() {
        binding.LoginBtn.setOnClickListener(view -> {

            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            if(!email.isEmpty() && !password.isEmpty()){
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        //task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignin(task);
        }
    }

    private void handleSignin(Task<GoogleSignInAccount> task ){
        GoogleSignInAccount account = task.getResult();
        if(account!=null){
            Log.d(TAG, "name" + account.getDisplayName()) ;
        }
    }
}