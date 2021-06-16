package com.almingiphy.giphyapp.data.model;

import java.io.Serializable;
import java.util.List;

import com.almingiphy.giphyapp.activities.MainActivity;
import com.almingiphy.giphyapp.data.model.data.Data;
import com.almingiphy.giphyapp.data.model.meta.Meta;
import com.almingiphy.giphyapp.data.model.pagination.Pagination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiphyModel implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
