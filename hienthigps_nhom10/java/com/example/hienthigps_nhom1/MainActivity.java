package com.example.hienthigps_nhom1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Button kiemtra;
    Switch coi,khoa;
    TextView trangthai;
    Boolean ktra = true;
    private firebase_change Firebase_listener;
    private DatabaseReference xedap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        kiemtra = findViewById(R.id.btktra);
        coi = findViewById(R.id.swcoi);
        khoa = findViewById(R.id.swkhoa);
        trangthai = findViewById(R.id.txttthai);
        xedap= FirebaseDatabase.getInstance().getReference("xe");
        Firebase_listener = new firebase_change("xe", new firebase_change.OnBikeDataChangeListener() {
            @Override
            public void onSpeedChanged(double speed) {
                if(khoa.isChecked() && speed>3 && ktra==true)
                {
                    trangthai.setText("Trạng thái: Xe đang di chuyển");
                    Notifications.showWarning(MainActivity.this);
                    Notifications.scheduleRepeatNotification(MainActivity.this);
                    ktra=false;
                }
                else
                {
                    if(speed<5)
                    {
                        ktra=true;
                        trangthai.setText("Trạng thái: Xe đang đứng yên");
                    }
                    else if(!khoa.isChecked())
                    {
                        ktra=true;
                    }
                }
            }

            @Override
            public void onCoiChanged(boolean isOn) {
                if(isOn) coi.setChecked(true);
                else coi.setChecked(false);
            }

            @Override
            public void onKhoaChanged(boolean isLocked) {
                if(isLocked) khoa.setChecked(true);
                else {
                    khoa.setChecked(false);
                }
            }
        });
        kiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Hienthi_vitri.class);
                startActivity(i);
            }
        });
        khoa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xedap.child("khoa").setValue(isChecked);
                if(!khoa.isChecked())
                {
                    Notifications.Unlock(MainActivity.this);
                    Notifications.cancelRepeatNotification(MainActivity.this);
                }
            }
        });

        coi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xedap.child("coi").setValue(isChecked);
                if (coi.isChecked()) {
                    Notifications.cancelRepeatNotification(MainActivity.this);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.unlock); // Chuông báo động mặc định

            //đặt mức ưu tiên báo động
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            //khai báo kênh thông báo
            NotificationChannel channel = new NotificationChannel(
                    "alarm_unlock",
                    "Canh bao mo khoa",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setSound(soundUri, audioAttributes);
            channel.enableVibration(true);
            //Chuông báo động 2
            Uri soundUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dichuyenbt); // Chuông báo động mặc định

            //đặt mức ưu tiên báo động
            AudioAttributes audioAttributes1 = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            //khai báo kênh thông báo
            NotificationChannel channel1 = new NotificationChannel(
                    "alarm_matxe",
                    "Canh bao mat xe",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setSound(soundUri1, audioAttributes1);
            channel1.enableVibration(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.createNotificationChannel(channel1);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Firebase_listener.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Firebase_listener.stopListening();
    }
}