package com.almingiphy.giphyapp.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.almingiphy.giphyapp.R;
import com.almingiphy.giphyapp.app.GiphyApp;

public class SplashActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startMainActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {

            GiphyApp.app().getNavigator().navigateToMain(SplashActivity.this);

        }, 1000);
    }
}
