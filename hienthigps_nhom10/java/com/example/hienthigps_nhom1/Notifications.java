package com.example.hienthigps_nhom1;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifications {
    @SuppressLint("MissingPermission")
    public static void showWarning(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_matxe")
                .setSmallIcon(R.drawable.notifications_active_24px)
                .setContentTitle("Cảnh báo")
                .setContentText("Xe đang di chuyển bất thường!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(1001, builder.build());
    }
    @SuppressLint("MissingPermission")
    public static void Unlock(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_unlock")
                .setSmallIcon(R.drawable.notifications_active_24px)
                .setContentTitle("Thông báo")
                .setContentText("Đã mở khóa!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(1001, builder.build());
    }
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleRepeatNotification(Context context) {
        Intent intent = new Intent(context, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerTime = System.currentTimeMillis() +  10 * 1000; // sau 3 phút

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }

    public static void cancelRepeatNotification(Context context) {
        Intent intent = new Intent(context, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
