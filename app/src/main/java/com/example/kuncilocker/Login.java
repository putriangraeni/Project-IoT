package com.example.kuncilocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView btn_lupaPass, btn_daftar2, txtForgetPass;
    EditText etEmail, etPassword;
    private FirebaseAuth auth;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_lupaPass = findViewById(R.id.btn_lupapassword_id);
        btn_daftar2 = findViewById(R.id.txt_daftar_id);
        txtForgetPass = findViewById(R.id.btn_lupapassword_id);
        btnLogin = findViewById(R.id.button_login_id);
        etEmail=findViewById(R.id.email_login_id);
        etPassword=findViewById(R.id.password_login_id);


        auth = FirebaseAuth.getInstance();

        // Menghilangkan action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnLogin.setOnClickListener(view -> {
            ProgressDialog loadingDialog = ProgressDialog.show(this, "Mengunggah",
                    "Harap tunggu...", true);
            auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    });
        });
        btn_daftar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftar = new Intent(Login.this, SignUp.class);
                startActivity(daftar);
                finish();
            }
        });

        // LUPA PASSWORD
        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forget_pass, null);
                EditText emailBox = dialogView.findViewById(R.id.edt_email_id);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btn_reset_id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(Login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(Login.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btn_cancel_id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }
}