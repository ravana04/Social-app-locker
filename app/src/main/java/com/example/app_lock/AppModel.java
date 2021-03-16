package com.example.app_lock;

import android.graphics.drawable.Drawable;

public class AppModel {

    String appname;
    Drawable appicon;
    int status; // lock or unlock (1 or 0)
    String packagename;
//    int id;

    public AppModel(String appname, Drawable appicon, int status, String packagename) {
        this.appname = appname;
        this.appicon = appicon;
        this.status = status;
        this.packagename = packagename;
    }



    public AppModel(){}



    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getAppicon() {
        return appicon;
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = appicon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) { this.packagename = packagename; }
}
