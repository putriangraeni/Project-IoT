package com.example.kuncilocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TampilanAwal extends AppCompatActivity {

    Button btn_masuk, btn_daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_awal);

        btn_masuk = findViewById(R.id.masuk_id);
        btn_daftar = findViewById(R.id.daftar_id);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        // Menghilangkan action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formMasuk = new Intent(TampilanAwal.this, Login.class);
                startActivity(formMasuk);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formDaftar = new Intent(TampilanAwal.this, SignUp.class);
                startActivity(formDaftar);
            }
        });
    }
}