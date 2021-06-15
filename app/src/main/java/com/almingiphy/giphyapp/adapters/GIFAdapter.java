package com.almingiphy.giphyapp.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almingiphy.giphyapp.R;
import com.almingiphy.giphyapp.data.model.data.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class GIFAdapter extends RecyclerView.Adapter<GIFAdapter.ViewHolder> {

    private List<Data> gifs;
    private Activity activity;

    public GIFAdapter(Activity activity) {
        this.activity = activity;
        this.gifs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_gif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Data gif = gifs.get(position);

        Glide.with(activity).load(gif.getImages().getFixedHeightSmall().getUrl())
                .apply(RequestOptions.fitCenterTransform()).into(holder.gifImage);

        holder.gifImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGIFInFullScreen(gif.getImages().getOriginal().getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gifs.size();
    }

    public void add(Data gif) {
        gifs.add(gif);
        notifyItemInserted(gifs.size() - 1);
    }

    public void addAll(List<Data> gifs) {
        for (Data gif : gifs) {
            add(gif);
        }
    }

    public void removeAll() {
        gifs.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView gifImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gifImage = itemView.findViewById(R.id.gif_image);
        }
    }

    public void showGIFInFullScreen(String gifUrl) {
        Dialog fullscreenDialog = new Dialog(activity, android.R.style.ThemeOverlay);
        fullscreenDialog.setContentView(R.layout.popup_gif_fullscreen);

        ImageView gifImageFullScreen = fullscreenDialog.findViewById(R.id.gif_image_full_screen);
        ProgressBar progressBar = fullscreenDialog.findViewById(R.id.progress_bar_popup);
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(activity).load(gifUrl).apply(RequestOptions.fitCenterTransform()).into(gifImageFullScreen);

        gifImageFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
            }
        });

        progressBar.setVisibility(View.GONE);

        fullscreenDialog.show();
    }
}