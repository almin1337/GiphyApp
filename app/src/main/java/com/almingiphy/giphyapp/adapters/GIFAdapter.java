package com.almingiphy.giphyapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almingiphy.giphyapp.R;
import com.almingiphy.giphyapp.data.model.GiphyModel;
import com.almingiphy.giphyapp.data.model.data.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class GIFAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<Data> gifs;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public GIFAdapter(Activity activity) {
        this.activity = activity;
        this.gifs = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_gif, parent, false);
                viewHolder = new GIFViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Data gif = gifs.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                GIFViewHolder gifViewHolder = (GIFViewHolder) holder;
                Glide.with(activity).load(gif.getImages().getOriginal().getUrl()).apply(RequestOptions.centerCropTransform()).into(gifViewHolder.gifImage);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return gifs == null ? 0 : gifs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == gifs.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = gifs.size() - 1;
        Data gif = getItem(position);

        if (gif != null) {
            gifs.remove(position);
            notifyItemRemoved(position);
        }
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

    public Data getItem(int position) {
        return gifs.get(position);
    }

    public class GIFViewHolder extends RecyclerView.ViewHolder {

        private ImageView gifImage;

        public GIFViewHolder(View itemView) {
            super(itemView);
            gifImage = itemView.findViewById(R.id.gif_image);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
        }
    }
}