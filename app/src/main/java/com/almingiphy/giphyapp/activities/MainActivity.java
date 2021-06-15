package com.almingiphy.giphyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.almingiphy.giphyapp.R;
import com.almingiphy.giphyapp.adapters.GIFAdapter;
import com.almingiphy.giphyapp.app.GiphyApp;
import com.almingiphy.giphyapp.data.model.GiphyModel;
import com.almingiphy.giphyapp.utils.APIUtils;
import com.almingiphy.giphyapp.utils.Constants;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private static final Integer GIFS_LIMIT = 10;

    private GiphyModel giphyModel;
    private GIFAdapter gifAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noGifs;
    private EditText searchMenu;
    private ProgressBar progressBar;
    private Integer offset = 0;
    private Call<GiphyModel> call;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        noGifs = findViewById(R.id.no_gifs);

        searchMenu = findViewById(R.id.search_menu);
        searchMenu.setEnabled(true);
        searchMenu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard(MainActivity.this, getCurrentFocus());
                }
                return false;
            }
        });

        gifAdapter = new GIFAdapter(MainActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_gifs);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(gifAdapter);
        setupUI(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                progressBar.setVisibility(View.VISIBLE);
                loadGIFs(searchMenu.getText().toString());
            }
        });

        RxTextView.textChanges(searchMenu)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textChanged -> {
                    offset = 0;
                    gifAdapter.removeAll();
                    loadGIFs(searchMenu.getText().toString());
                });
    }

    @Override
    public void onRefresh() {
        offset = 0;
        gifAdapter.removeAll();
        loadGIFs(searchMenu.getText().toString());
    }

    private void loadGIFs(String search) {
        if (!search.equals(""))
            call = GiphyApp.app().service().searchGIFs(Constants.API_KEY, search, GIFS_LIMIT, offset); //Search GIFs if user typed something
        else
            call = GiphyApp.app().service().getTrendingGIFs(Constants.API_KEY, GIFS_LIMIT, offset); //Trending GIFs

        call.enqueue(new Callback<GiphyModel>() {
            @Override
            public void onResponse(@NonNull Call<GiphyModel> call, @NonNull Response<GiphyModel> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "response GIFs: " + new Gson().toJson(response.body()));

                    giphyModel = response.body();
                    if (giphyModel != null) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                        if (giphyModel.getData().isEmpty() && offset == 0)
                            noGifs.setVisibility(View.VISIBLE);
                        else
                            noGifs.setVisibility(View.GONE);

                        if (giphyModel.getData().size() < GIFS_LIMIT) return;

                        gifAdapter.addAll(giphyModel.getData());
                        offset += giphyModel.getData().size();
                        progressBar.setVisibility(View.GONE);
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

    private void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(MainActivity.this, view);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}