package com.example.app_lock;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.example.app_lock.Notification;

public class Backgound_Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1337;
    private static String TAG = "Service";
    private static Backgound_Service mCurrentService;
    private int counter = 0;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compname;
    private Context mContext;
    SharedPreferences sharedpreferences;
    DatabaseHandler db;
    List<AppModel> lockedAppList = new ArrayList<>();
    public Backgound_Service() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Context mContext = getApplicationContext();


        devicePolicyManager=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager=(ActivityManager) getSystemService(ACTIVITY_SERVICE );
        compname=new ComponentName(this,MyAdmin.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;

        timer = new Timer("AppCheckServices");
        timer.schedule(updateTask, 1000L, 1000L);

    }


public  ArrayList<String> Arrayl(){
    ArrayList<String> lockname = new ArrayList<String>();
    db = new DatabaseHandler(this);
    lockedAppList = db.getAllLockedApps();
    for(int i =0; i<lockedAppList.size(); i++) {
//        Log.e("Lockeddddd", lockedAppList.get(i).getAppname());
        lockname.add(lockedAppList.get(i).getAppname());
    }
    return  lockname;
}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int timer(String currentApp) throws PackageManager.NameNotFoundException {
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
        int call=-1;
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            Integer i = 0;

            for (UsageStats usageStats : stats) {

                TimeInforground = usageStats.getTotalTimeInForeground();
                PackageName = usageStats.getPackageName();

                if (PackageName.equals(currentApp)) {
                    PackageManager packageManager1 = getApplicationContext().getPackageManager();
                    String appName = (String) packageManager1.getApplicationLabel(packageManager1.getApplicationInfo(PackageName, PackageManager.GET_META_DATA));
                     call = (int) TimeInforground;
                    // String t= getPackageManager().getApplicationLabel(usageStats).toString();
                    // Log.i("try",t);
                    minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
                    seconds = (int) (TimeInforground / 1000) % 60;
                    hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
                    Log.i("BAC", "PackageName is" + appName + " Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s");
                    // }

                }
            }
        }
        return call;
    }
    public  int share(){
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        int hr = sharedpreferences.getInt("Hr",0);
        int min = sharedpreferences.getInt("Min", 0);
        int tt =  (hr*60*60*1000)+(min*60*1000);
        return tt;
    }

    private TimerTask updateTask = new TimerTask() {

        @Override
        public void run() {
            ArrayList<String> lockn = new ArrayList<String>();
            int a = 1;
            lockn = Arrayl();
//            Log.e("List", "Current App time: " + lockn);
            String currentApp = "NULL";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                @SuppressLint("WrongConstant") UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
                long time = System.currentTimeMillis();
                List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                if (appList != null && appList.size() > 0) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : appList) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();



                        PackageManager packageManager1= getApplicationContext().getPackageManager();
                        try {
                            String appName = (String) packageManager1.getApplicationLabel(packageManager1.getApplicationInfo(currentApp, PackageManager.GET_META_DATA));
//                            Log.e("2222", "Current App  " +"   "+appName);
                            if(lockn.contains(appName)){
                                int ctime = timer(currentApp);
                                Log.e("timer222", "Current App time: " + ctime);
                                int check = share();
                                Log.e("checktime", "Current App time: " + check);
                                if(ctime>check){
//                                    Log.i("LOCKED", "LOCKED " + currentApp);
                                    devicePolicyManager.lockNow();
                                    try {
                                   Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                }

                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }



                    }
                }
                else {
//                    ActivityManager am =(ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                    List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(5);
//
//                    ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
//                    Log.e("ada", "Current App in foreground is: " + tasks.get(0)+tasks.get(1));
//                    ComponentName rootActivity = task.baseActivity;
//                    rootActivity.getPackageName();
//                    Log.e("ada", "Current App in foreground is: " + currentApp + rootActivity.getPackageName());

                    ActivityManager mActivityManager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

                    if(Build.VERSION.SDK_INT > 20){
                        String mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
//                        Log.e(TAG, "Current App in foreground is: " + mPackageName);
                    }
                    else{
                        String mpackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//                        Log.e(TAG, "Current App in foreground is: " + mpackageName);
                    }

                   //Log.e(TAG, "Current App in foreground is: " + mpackageName);
                    }
            }

            else {
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                currentApp = tasks.get(0).processName;
                 }
           // Log.e("adapter2222", "Current App in foreground is: " + currentApp);
            // Thread.sleep(1000);
        }

    };



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;
        Toast.makeText(getApplicationContext(),"This is a Service running in Background",Toast.LENGTH_LONG).show();
        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            Toast.makeText(getApplicationContext(),"Restarting in Background",Toast.LENGTH_LONG).show();
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        //startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * it starts the process in foreground. Normally this is done when screen goes off
     * THIS IS REQUIRED IN ANDROID 8 :
     * "The system allows apps to call Context.startForegroundService()
     * even while the app is in the background.
     * However, the app must call that service's startForeground() method within five seconds
     * after the service is created."
     */
    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "restarting foreground");
            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Service notification", "This is the service's notification", R.drawable.logonewround));
                Log.i(TAG, "restarting foreground successful");
                //startTimer();
            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }


    /**
     * this is called when the process is killed by Android
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved called");
        startService(new Intent(this, Backgound_Service.class));
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        restartForeground();
        // do not call stoptimertask because on some phones it is called asynchronously
        // after you swipe out the app and therefore sometimes
        // it will stop the timer after it was restarted
        // stoptimertask();
    }


    /**
     * static to avoid multiple timers to be created when the service is called several times
     */
    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime = 0;
    public void startTimer() {
        Log.i(TAG, "Starting timer");

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        Log.i(TAG, "Scheduling...");
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        Log.i(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static Backgound_Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Backgound_Service mCurrentService) {
        Backgound_Service.mCurrentService = mCurrentService;
    }


}