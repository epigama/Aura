package com.deco.moodify;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    public static final int splash_screen_timer = 2000;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        //  setTheme(R.style.dark_theme);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();


        View decorView = getWindow().getDecorView();

        // Hide the status bar.

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        new Handler().postDelayed(new Runnable() {

            @Override

            public void run() {

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);

                startActivity(intent);

            }

        }, splash_screen_timer);


    }



}

