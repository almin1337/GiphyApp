package com.almingiphy.giphyapp.navigator;

import android.app.Activity;

import com.almingiphy.giphyapp.activities.MainActivity;
import com.almingiphy.giphyapp.data.model.GiphyModel;

public class Navigator implements GiphyNavigator {

    @Override
    public void navigateToMain(Activity activity) {
        activity.startActivity(MainActivity.createIntent(activity));
        activity.finish();
    }

    @Override
    public void navigateToMain(Activity activity, GiphyModel giphyModel) {
        activity.startActivity(MainActivity.createIntent(activity, giphyModel));
        activity.finish();
    }
}
