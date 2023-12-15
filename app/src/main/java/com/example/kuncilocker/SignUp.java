package com.example.kuncilocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    Button btnSignup;
    EditText etUsername, etEmail, etPassword, etNim, etPasswordConfirmation;
    TextInputLayout tilUsername, tilEmail, tilPassword, tilNim, tilPasswordConfirmation;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView btn_login2 = findViewById(R.id.txt_login_id);
        btnSignup = findViewById(R.id.button_daftar_id);
        etUsername=findViewById(R.id.username_daftar_id);
        etEmail=findViewById(R.id.email_daftar_id);
        etPassword=findViewById(R.id.password_daftar_id);
        etPasswordConfirmation=findViewById(R.id.konfirmasiPassword_daftar_id);
        etNim=findViewById(R.id.nim_daftar_id);
        tilUsername=findViewById(R.id.til_username);
        tilEmail=findViewById(R.id.til_email);
        tilNim=findViewById(R.id.til_nim);
        tilPassword=findViewById(R.id.til_password);
        tilPasswordConfirmation=findViewById(R.id.til_password_confirmation);
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(view -> {
            if(isValid()) {
                ProgressDialog loadingDialog = ProgressDialog.show(this, "Mengunggah",
                        "Harap tunggu...", true);
                auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(SignUp.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(etUsername.getText().toString())
                                        .build();
                                user.updateProfile(profileUpdates);
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                DatabaseReference currentUserRef = usersRef.child(user.getUid());
                                currentUserRef.child("nim").setValue(etNim.getText().toString())
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this,"Registration failed. Please try again.",Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        });
                            } else {
                                Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        })

                ;
            }
        });

        // Menghilangkan action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btn_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(SignUp.this, Login.class);
                startActivity(login);
                finish();
            }
        });
    }

    private boolean isValid() {
        boolean valid = true;
        if(etUsername.getText().toString().isEmpty()){
            tilUsername.setError("Fill the username");
            valid=false;
        }else{
            tilUsername.setErrorEnabled(false);
        }
        if(etEmail.getText().toString().isEmpty()){
            tilEmail.setError("Fill the email");
            valid=false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            tilEmail.setError("Email is not valid");
        }else{
            tilEmail.setErrorEnabled(false);
        }
        if(etNim.getText().toString().isEmpty()){
            tilNim.setError("Fill the NIM");
            valid=false;
        }else{
            tilNim.setErrorEnabled(false);
        }
        if(etPassword.getText().toString().isEmpty()){
            tilPassword.setError("Fill the password");
            valid=false;
        }else{
            tilPassword.setErrorEnabled(false);
        }
        if(etPasswordConfirmation.getText().toString().isEmpty()){
            tilPasswordConfirmation.setError("Fill the password confirmation");
            valid=false;
        }else if(!etPasswordConfirmation.getText().toString().equals(etPassword.getText().toString())) {
            tilPasswordConfirmation.setError("Password do not match");
            valid=false;
        }else{
            tilPasswordConfirmation.setErrorEnabled(false);
        }
        return valid;
    }

}