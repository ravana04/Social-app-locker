package com.example.app_lock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn,btn2,btn3;

    String pass;

    static final  String KEY="pass";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pass = SharedPrefUtil.getInstance(this).getString(KEY);
        final Context con  = this;

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAccessGranted()) {

                    if(!pass.isEmpty()) {
                        startActivity(new Intent(MainActivity.this, ShowAllApps.class));
                    }
                    else {
                        Toast.makeText(con,"Set Password First",Toast.LENGTH_SHORT).show();
                    }
                }
            else {
                    Toast.makeText(MainActivity.this,"Please Allow App Usage Persmission",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);

            }
        });

        btn3 = findViewById(R.id.btn3);

        if(pass.isEmpty()){
            btn3.setText("Set Password");
        }
        else {
            btn3.setText("Update Password");
        }
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pass.isEmpty()){
                    setPassword(con);
                }
                else {
                    UpdatePassword(con);
                }
            }
        });

    }

    private  void setPassword(final Context con){

        AlertDialog.Builder dialog = new AlertDialog.Builder(con);

        LinearLayout ll = new LinearLayout(con);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(con);
        t1.setText("Enter Your Password");

        ll.addView(t1);


        final EditText input = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView(input);

        dialog.setView(ll);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Save PAssword
                SharedPrefUtil.getInstance(con).putString(KEY,input.getText().toString());
                Toast.makeText(con,"Password set Successfully",Toast.LENGTH_SHORT).show();
                pass = input.getText().toString();
                btn3.setText("Update Password");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private  void  UpdatePassword(final Context con){

        AlertDialog.Builder dialog = new AlertDialog.Builder(con);

        LinearLayout ll = new LinearLayout(con);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(con);
        t1.setText("Enter Previous Password");

        ll.addView(t1);


        final EditText input = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView(input);

        TextView t2 = new TextView(con);
        t2.setText("Enter New Password");

        ll.addView(t2);


        final EditText input2 = new EditText(con);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ll.addView(input2);

        dialog.setView(ll);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Save Password

                if (pass.equals(input.getText().toString())) {

                    SharedPrefUtil.getInstance(con).putString(KEY,input2.getText().toString());
                    Toast.makeText(con,"Password Updated Successfully",Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(con,"Password doesn't match",Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}