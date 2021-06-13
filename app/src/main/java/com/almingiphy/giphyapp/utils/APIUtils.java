package com.almingiphy.giphyapp.utils;

import android.util.Log;
import android.widget.Toast;

import com.almingiphy.giphyapp.app.GiphyApp;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class APIUtils {
    private static final String TAG = "APIUtils";

    public static void getErrorMessage(ResponseBody responseBody) {
        try {
            String errorMessage = responseBody.string();
            JSONObject jObjError = new JSONObject(errorMessage);
            Toast.makeText(GiphyApp.app(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(GiphyApp.app(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void onFailureHandling(Throwable t) {
        Log.e(TAG, "onFailure: ", t);
        if (t instanceof IOException) {
            Toast.makeText(GiphyApp.app(), "Please check your Internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GiphyApp.app(), "An Unexpected Error Occurred. Please Try Later.", Toast.LENGTH_SHORT).show();
        }
    }
}
