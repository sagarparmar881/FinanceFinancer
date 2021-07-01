package com.example.financefinancer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financefinancer.R;

public class SplashActivity extends AppCompatActivity {
    Animation anim;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageview = findViewById(R.id.logo);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", true);
        editor.commit();
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(getBaseContext(),TripActivity.class));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                startActivity(new Intent(getBaseContext(),TripActivity.class));

            }
        });

        imageview.startAnimation(anim);


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        if (!firstRun) {
            Intent intent = new Intent(this, TripActivity.class);
            startActivity(intent);
        }
    }
}
