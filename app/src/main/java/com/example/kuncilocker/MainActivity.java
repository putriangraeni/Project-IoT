package com.example.kuncilocker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.kuncilocker.model.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    ImageView imageView;
    TextView textView;
    TextView tvHistoryData, tvHistoryDate;
    String name;
    CardView cvHistory;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.about_id){
            Intent about = new Intent(MainActivity.this, About.class);
            startActivity(about);
        } else if (item_id == R.id.logout_id){
            auth.signOut();
            Intent logout = new Intent(MainActivity.this, Login.class);
            startActivity(logout);
            finish();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1_id);
        imageView = findViewById(R.id.imageView_id);
        switchCompat = findViewById(R.id.switch_id);
        tvHistoryData = findViewById(R.id.tv_history_data);
        tvHistoryDate = findViewById(R.id.tv_history_date);
        cvHistory = findViewById(R.id.cv_history);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }else{
            name=user.getDisplayName();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("brankas");
        DatabaseReference historyRef = database.getReference("history");
        auth = FirebaseAuth.getInstance();

        cvHistory.setOnClickListener(view -> {
            startActivity(new Intent(this,HistoryActivity.class));
        });
        historyRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d:snapshot.getChildren()){
                        setLatestHistory(d.getValue(History.class));
                        break;
                    }
                }else{
                    setLatestHistory(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                History h = new History(historyRef.push().getKey(), name, "Phone", System.currentTimeMillis(), isChecked?1:0);
                historyRef.child(h.getKey()).setValue(h)
                        .addOnSuccessListener(unused -> {
                            if (isChecked) {
                                // Mengganti gambar saat switch button dalam keadaan checked
                                imageView.setImageResource(R.drawable.bg_lock2);
                                // Mengganti text saat switch button dalam keadaan checked
                                textView.setText("Click To Lock");


                                myRef.setValue(1);
                                Log.d(TAG, "onCheckedChanged: nyala");
                                //Toast.makeText(MainActivity.this, "Loker terbuka", Toast.LENGTH_SHORT).show();
                                showNotification("Loker Terbuka");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        switchCompat.setChecked(false); // Set switchCompat menjadi false (0) setelah 8 detik
                                    }
                                }, 8000); // Menunda eksekusi kode di dalam run() selama 8 detik (8000 milidetik)

                            } else {
                                // Mengganti gambar saat switch button dalam keadaan checked
                                imageView.setImageResource(R.drawable.bg_lock1);
                                // Mengganti text saat switch button dalam keadaan checked
                                textView.setText("Click To Unclock");

                                myRef.setValue(0);
                                Log.d(TAG, "onCheckedChanged: mati");
                                //Toast.makeText(MainActivity.this, "Loker tertutup", Toast.LENGTH_SHORT).show();
                                showNotification("Loker Tertutup");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }


    private void setLatestHistory(History h){
        if(h!=null){
            tvHistoryData.setText(h.getName()+(h.getType()==1?" Membuka":" Menutup")+" Loker");
            tvHistoryDate.setText(new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("id", "ID")).format(new Date(h.getTime())));
        }
    }
    private void showNotification (String message){
        // Membuat ID unik untuk notifikasi
        int notificationId = 1;

        // Membuat Channel ID dan Nama Channel untuk notifikasi (hanya diperlukan di Android 8.0 Oreo ke atas)
        String channelId = "my_channel_id";
        String channelName = "My Channel";

        // Membuat objek NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_lock_24)
                .setContentTitle("Notifikasi")
                .setContentText(message)
                .setAutoCancel(true); // Agar notifikasi hilang setelah diklik

        // Mendapatkan referensi dari NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Memeriksa versi Android untuk menentukan apakah perlu membuat Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Membuat objek NotificationChannel
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription("Deskripsi Channel");

            // Menambahkan NotificationChannel ke NotificationManager
            notificationManager.createNotificationChannel(channel);
        }

        // Menampilkan notifikasi
        notificationManager.notify(notificationId, builder.build());
    }
}