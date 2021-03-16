package com.example.app_lock;

import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Freq_used_app extends AppCompatActivity
{
    ListView lview;
    List<AppModel> appModelList = new ArrayList<>();
    DatabaseHandler db;
    AppAdapter adapter;
    ProgressDialog progressDialog;
    SearchView searchView;
    List<String> appnamelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freq_layout);


        lview = findViewById(R.id.lv5);
        adapter = new AppAdapter(this, R.layout.adapter_design,appModelList);
        lview.setAdapter(adapter);


        progressDialog= new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onShow(DialogInterface dialog) {

                try {
                    getInstalledapps();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    @Override
    protected void onResume()
    {
        super.onResume();
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<String> timer() throws PackageManager.NameNotFoundException {
        String PackageName = "Nothing";

        long TimeInforground = 500;

        int minutes = 500, seconds = 500, hours = 500;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        long time = System.currentTimeMillis();
        List<String> score = new ArrayList<String>();
        int a=0;
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        ArrayList<String> values = new ArrayList<String>();

        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            Integer i = 0;
            for (UsageStats usageStats : stats) {
                TimeInforground = usageStats.getTotalTimeInForeground();
                PackageName = usageStats.getPackageName();
                PackageManager packageManager1= getApplicationContext().getPackageManager();
                String appName = (String) packageManager1.getApplicationLabel(packageManager1.getApplicationInfo(PackageName, PackageManager.GET_META_DATA));
                minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
                seconds = (int) (TimeInforground / 1000) % 60;
                hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
               // Log.i("BAC", "PackageName is" + appName + " Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s");
                if(TimeInforground!=0){
                    a=a+1;
                   values.add(appName);
                }
                i = i + 1;
            }
            }
    return values;
        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getInstalledapps() throws PackageManager.NameNotFoundException {
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        db = new DatabaseHandler(this);
        ArrayList<String> useapp = new ArrayList<String>();
        useapp=timer();
        for(int i=0; i<packageInfos.size(); i++) {
            String name = packageInfos.get(i).applicationInfo.loadLabel(getPackageManager()).toString();
            Drawable icon = packageInfos.get(i).applicationInfo.loadIcon(getPackageManager());
            String packagename = packageInfos.get(i).packageName;
            if(useapp.contains(name)) {
                appModelList.add(new AppModel(name, icon, 0, packagename));
                appnamelist.add(name);
                db.addApp(new AppModel(name, icon, 0, packagename));
            }
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}