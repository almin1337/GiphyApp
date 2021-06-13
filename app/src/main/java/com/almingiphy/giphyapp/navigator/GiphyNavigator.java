package com.almingiphy.giphyapp.navigator;

import android.app.Activity;

import com.almingiphy.giphyapp.data.model.GiphyModel;

public interface GiphyNavigator {

    void navigateToMain(Activity activity);

    void navigateToMain(Activity activity, GiphyModel giphyModel);
}
