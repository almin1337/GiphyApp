package com.almingiphy.giphyapp.navigator;

import android.app.Activity;

import com.almingiphy.giphyapp.activities.MainActivity;

public class Navigator implements GiphyNavigator {

    @Override
    public void navigateToMain(Activity activity) {
        activity.startActivity(MainActivity.createIntent(activity));
        activity.finish();
    }
}
