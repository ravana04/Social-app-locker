package com.example.app_lock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Main_Menu extends AppCompatActivity implements View.OnClickListener {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    private static Intent serviceIntent = null;
    Context context1;

    private Button  eb;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

    Button ballapp,blocapp,btimeuse,babtus;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main__menu);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);


        eb = (Button) findViewById(R.id.enable);

        eb.setOnClickListener(this);
       // SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
       //sharedpreferences.edit().clear().apply();



        ballapp = findViewById(R.id.allapp);
        blocapp = findViewById(R.id.locapp);
        btimeuse = findViewById(R.id.timeuse);
        babtus = findViewById(R.id.abtus);

        ballapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Home Panel open", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main_Menu.this, ShowAllApps.class));
            }
        });

        blocapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Locked Panel open", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main_Menu.this, ShowAllLockedApps.class));
            }
        });

        btimeuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Time Usage open", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main_Menu.this, Time_used.class));
            }
        });

        babtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "About Panel open", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main_Menu.this, aboutus.class));
            }
        });
    toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    nav= (NavigationView)findViewById(R.id.navmenu);

    drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

    toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
        drawerLayout.openDrawer(GravityCompat.START);

    nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.freq: {
                    Toast.makeText(getApplicationContext(), "Home Panel open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main_Menu.this, Freq_used_app.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }

                case R.id.all_app: {
                    Toast.makeText(getApplicationContext(), "Home Panel open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main_Menu.this, ShowAllApps.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.time_use:
                    Toast.makeText(getApplicationContext(), "Time Usage open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main_Menu.this, Time_used.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.locked:
                    Toast.makeText(getApplicationContext(), "Locked Panel open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main_Menu.this, ShowAllLockedApps.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.about:
                    Toast.makeText(getApplicationContext(), "About Panel open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main_Menu.this, aboutus.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        }
    });


    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        eb.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    public void onClick(View view) {
        if (view == nav) {
            boolean active = devicePolicyManager.isAdminActive(compName);

            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
            }

        } else if (view == eb) {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent, RESULT_ENABLE);

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(Main_Menu.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main_Menu.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}