package com.example.app_lock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowAllLockedApps extends AppCompatActivity
{
    ListView lview;
    List<AppModel> lockedAppList = new ArrayList<>();
    DatabaseHandler db;
    LockedAppAdapter adapter;
    ProgressDialog progressDialog;
    Context context;
    EditText h,m;
    Button b;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int hour =0, minutes =0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_locked_apps);
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        if(!granted){
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        if ( !isMyServiceRunning(Backgound_Service.class)){
            startService(new Intent(this, Backgound_Service.class));
        }


        lview = findViewById(R.id.lv2);
        h = findViewById(R.id.hr);
        m = findViewById(R.id.min);
        b = findViewById(R.id.set);

        db = new DatabaseHandler(this);
        lockedAppList = db.getAllLockedApps();
//        for(int i =0; i<lockedAppList.size(); i++)
//            Log.e("Lockeddddd", lockedAppList.get(i).getAppname());

        adapter = new LockedAppAdapter(ShowAllLockedApps.this,R.layout.locked_row, lockedAppList);
        lview.setAdapter(adapter);

        progressDialog= new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getLockedapps();
            }
        });

        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt("Flag2",0);
        editor.apply();
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String today = sharedpreferences.getString("Today", "");
        String tomorrow = sharedpreferences.getString("Tomorrow", "");

        if(! tomorrow.equals("")){
            Date todaysdate = new Date();
            SimpleDateFormat currentdate = new SimpleDateFormat("yyyy-MM-dd");
            String date = currentdate.format(todaysdate);
            editor.putString("Today", date);
            editor.apply();}
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdformat.parse(today);

            d2 = sdformat.parse(tomorrow);

            if(d1.compareTo(d2) < 0) {
                b.setEnabled(false);
                editor.putInt("Flag", 1);
            }
            else
            {
                b.setEnabled(true);
                editor.putInt("Flag", 0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        editor.apply();
        int checkhr = sharedpreferences.getInt("Hr", 0);
        int checkmin = sharedpreferences.getInt("Min", 0);
     h.setText(String.valueOf(checkhr));
        m.setText(String.valueOf(checkmin));


    }

    public void getLockedapps()
    {
        db = new DatabaseHandler(this);
        lockedAppList = db.getAllLockedApps();
//        for(int i =0; i<lockedAppList.size(); i++)
//            Log.e("Lockeddddd", lockedAppList.get(i).getAppname());

        progressDialog.dismiss();
    }

    public void setTimer(View view) {

        if (!h.getText().toString().equals(""))
            hour = Integer.parseInt(h.getText().toString());
        if (!m.getText().toString().equals(""))
            minutes = Integer.parseInt(m.getText().toString());

        editor = sharedpreferences.edit();
        Date todaysdate1 = new Date();
        Date tomorrow = new Date(todaysdate1.getTime() + (1000 * 60 * 60 * 24));
        SimpleDateFormat currentdate1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = currentdate1.format(tomorrow);
        editor.putString("Tomorrow", date1);
        editor.putInt("Hr", hour);
        editor.putInt("Min", minutes);
        editor.apply();

        b.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                b.setEnabled(true);
            }
        }, 86400000);// set time as per your requirement
        editor.putInt("Flag",1);
        editor.apply();
    }
}