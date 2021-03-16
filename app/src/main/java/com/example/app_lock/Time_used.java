package com.example.app_lock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Time_used extends AppCompatActivity {

    private ListView list;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_used);
        list = (ListView) findViewById(R.id.listv);

        try {
            timer();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void timer() throws PackageManager.NameNotFoundException {
        String PackageName = "Nothing";

        long TimeInforground = 500;

        int minutes = 500, seconds = 500, hours = 500;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        long time = System.currentTimeMillis();
        List<String> score = new ArrayList<String>();
        int a=0;
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        String[] values = new String[stats.size()];
        int[] tsort = new int[stats.size()];
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            Integer i = 0;
            for (UsageStats usageStats : stats) {

                TimeInforground = usageStats.getTotalTimeInForeground();
                PackageName = usageStats.getPackageName();

                //   if(PackageName.equals("com.facebook.katana")) {
                PackageManager packageManager1= getApplicationContext().getPackageManager();
                String appName = (String) packageManager1.getApplicationLabel(packageManager1.getApplicationInfo(PackageName, PackageManager.GET_META_DATA));

                // String t= getPackageManager().getApplicationLabel(usageStats).toString();
                // Log.i("try",t);
                minutes = (int) ((TimeInforground / (1000 * 60)) % 60);

                seconds = (int) (TimeInforground / 1000) % 60;

                hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
                Log.i("BAC", "PackageName is" + appName + " Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s");
                // }
                if(TimeInforground!=0){
                    a=a+1;
                }
                tsort[i] = (int) TimeInforground;
                values[i] = appName + " Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s";
                i = i + 1;
            }



            int b =0;
            String[] valuef = new String[a];
            Integer[] sortf = new Integer[a];
            for (int j = 0; j < values.length; ++j){
                if(tsort[j]!=0){
                    sortf[b]=tsort[j];
                    valuef[b]=values[j];
                    b=b+1;
                }
            }

            int n = tsort.length;
            for (int j = 1; j < sortf.length; ++j) {
                int key = sortf[j];
                String key1 = valuef[j];
                int k = j - 1;

                while (k >= 0 && sortf[k] < key) {
                    sortf[k + 1] = sortf[k];
                    valuef[k+1]=valuef[k];
                    k = k - 1;
                }
                sortf[k + 1] = key;
                valuef[k+1]=key1;
            }
            final ArrayList<time_layout> arrayList = new ArrayList<time_layout>();
            for (int j = 0; j < valuef.length; ++j){
                arrayList.add(new time_layout(valuef[j]));
            }

            NumbersViewAdapter numbersArrayAdapter = new NumbersViewAdapter(this, arrayList);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, valuef);
            list.setAdapter(numbersArrayAdapter);
        }
    }
}