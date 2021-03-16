package com.example.app_lock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.sql.Time;

public class Intro extends AppCompatActivity {
ImageView lgo,splash;
LottieAnimationView lottieAnimationView;
TextView lohoname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        lgo = findViewById(R.id.ll);
        splash = findViewById(R.id.img);
        lohoname=findViewById(R.id.namell);
        lottieAnimationView= findViewById(R.id.lottieAnimationView2);

        splash.animate().translationY(-2000).setDuration(1000).setStartDelay(3000);
        lgo.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        lohoname.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Intro.this,Main_Menu.class));
                finish();
            }
        },4000);


    }

}