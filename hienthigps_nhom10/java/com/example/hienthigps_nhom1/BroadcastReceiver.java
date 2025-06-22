package com.example.hienthigps_nhom1;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.*;
public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private DatabaseReference xedap;
    @Override
    public void onReceive(Context context, Intent intent) {
        MyConditionChecker.vanMoKhoa(context, new MyConditionChecker.ConditionCheckCallback() {
            @Override
            public void onConditionResult(boolean result) {
                if (result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("alarm_matxe", "Alarm Alert", NotificationManager.IMPORTANCE_HIGH);
                    }
                    Log.d("MyReceiver", "Broadcast received!");
                    Notifications.showWarning(context); // Gửi lại thông báo
                    Notifications.scheduleRepeatNotification(context); // Lặp lại tiếp
                }
            }
        });
    }
}
