package com.example.hienthigps_nhom1;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class firebase_change {
    private DatabaseReference xedapRef;
    private ValueEventListener speedListener, coiListener, khoaListener;

    public interface OnBikeDataChangeListener {
        void onSpeedChanged(double speed);
        void onCoiChanged(boolean isOn);
        void onKhoaChanged(boolean isLocked);
    }

    private OnBikeDataChangeListener listener;

    public firebase_change(String bikeId, OnBikeDataChangeListener listener) {
        this.xedapRef = FirebaseDatabase.getInstance().getReference(bikeId);
        this.listener = listener;
    }
    public void startListening() {
        // Speed Listener
        speedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double vtoc = snapshot.getValue(Double.class);
                if (vtoc != null && listener != null) {
                    listener.onSpeedChanged(vtoc);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        // Còi Listener
        coiListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean c = snapshot.getValue(Boolean.class);
                if (c != null && listener != null) {
                    listener.onCoiChanged(c);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        // Khóa Listener
        khoaListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean key = snapshot.getValue(Boolean.class);
                if (key != null && listener != null) {
                    listener.onKhoaChanged(key);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        xedapRef.child("speed").addValueEventListener(speedListener);
        xedapRef.child("coi").addValueEventListener(coiListener);
        xedapRef.child("khoa").addValueEventListener(khoaListener);
    }

    public void stopListening() {
        if (speedListener != null) xedapRef.child("speed").removeEventListener(speedListener);
        if (coiListener != null) xedapRef.child("coi").removeEventListener(coiListener);
        if (khoaListener != null) xedapRef.child("khoa").removeEventListener(khoaListener);
    }
}

