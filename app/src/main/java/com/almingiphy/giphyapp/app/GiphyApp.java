package com.almingiphy.giphyapp.app;

import android.app.Application;

import com.almingiphy.giphyapp.data.GiphyFactory;
import com.almingiphy.giphyapp.data.GiphyService;
import com.almingiphy.giphyapp.navigator.GiphyNavigator;
import com.almingiphy.giphyapp.navigator.Navigator;

public class GiphyApp extends Application {

    private static GiphyApp context;
    private GiphyService service;
    private GiphyNavigator navigator;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        service = GiphyFactory.create();
        navigator = new Navigator();
    }

    public static GiphyApp app() {
        return context;
    }

    public GiphyService service() {
        if (service == null) {
            return GiphyFactory.create();
        }
        return service;
    }

    public GiphyNavigator getNavigator() {
        return navigator;
    }
}