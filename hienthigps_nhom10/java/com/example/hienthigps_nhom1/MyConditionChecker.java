package com.example.hienthigps_nhom1;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyConditionChecker {
    private static DatabaseReference xedap;
    static Double tocdo;
    static Boolean khoa;
    static Boolean ktra=false;
    public interface ConditionCheckCallback {
        void onConditionResult(boolean result);
    }
    public static void vanMoKhoa(Context context, ConditionCheckCallback conditionCheckCallback) {
        xedap = FirebaseDatabase.getInstance().getReference("xe");

        xedap.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy từng trường riêng lẻ từ snapshot
//                Double tocdo = snapshot.child("speed").getValue(Double.class);
                Boolean khoa = snapshot.child("khoa").getValue(Boolean.class);
                // Kiểm tra điều kiện
                // Không mở khóa
                conditionCheckCallback.onConditionResult(khoa); // Khóa mở
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi đọc dữ liệu xe: " + error.getMessage());
                conditionCheckCallback.onConditionResult(false);
            }
        });
    }
}
