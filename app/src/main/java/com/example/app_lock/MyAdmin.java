package com.example.app_lock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyAdmin extends DeviceAdminReceiver {
    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context,"Device Admin : enabled",Toast.LENGTH_LONG).show();
    }


}
