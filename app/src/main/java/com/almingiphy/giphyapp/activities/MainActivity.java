package com.almingiphy.giphyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.almingiphy.giphyapp.R;
import com.almingiphy.giphyapp.adapters.GIFAdapter;
import com.almingiphy.giphyapp.app.GiphyApp;
import com.almingiphy.giphyapp.data.model.GiphyModel;
import com.almingiphy.giphyapp.listeners.PaginationScrollListener;
import com.almingiphy.giphyapp.utils.APIUtils;
import com.almingiphy.giphyapp.utils.Constants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String GIPHY = "giphy";
    private static final Integer GIFS_LIMIT = 11;

    private GiphyModel giphyModel;
    private GIFAdapter gifAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private boolean isLoading, isLastPage;
    private Integer offset = 0;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createIntent(Context context, GiphyModel giphyModel) { //NE TREBA???????????????
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(GIPHY, giphyModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);

        EditText searchMenu = findViewById(R.id.search_menu);
        searchMenu.setEnabled(true);
        searchMenu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard(MainActivity.this, getCurrentFocus());
                }
                return false;
            }
        });

        searchMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 3) {
//                    filterCity(s.toString());
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    gifAdapter = new GIFAdapter(MainActivity.this);
                    recyclerView.setAdapter(gifAdapter);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_gifs);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        gifAdapter = new GIFAdapter(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(gifAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                loadMoreGIFs();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadMoreGIFs();
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loadMoreGIFs() {
        Call<GiphyModel> call = GiphyApp.app().service().getTrendingGIFs(Constants.API_KEY, GIFS_LIMIT, offset);
        call.enqueue(new Callback<GiphyModel>() {
            @Override
            public void onResponse(@NonNull Call<GiphyModel> call, @NonNull Response<GiphyModel> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "response Trending GIFs: " + new Gson().toJson(response.body()));
                    progressBar.setVisibility(View.GONE);
//                    gifAdapter.removeLoadingFooter();
                    isLoading = false;

                    giphyModel = response.body();
                    if (giphyModel != null) {
                        gifAdapter.addAll(giphyModel.getData());
                        offset += giphyModel.getData().size();

                        if (giphyModel.getData().size() != GIFS_LIMIT) isLastPage = true;
//                        else gifAdapter.addLoadingFooter();
                    }
                } else {
                    APIUtils.getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GiphyModel> call, @NonNull Throwable t) {
                APIUtils.onFailureHandling(t);
            }
        });
    }
}