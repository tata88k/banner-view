package com.pacific.banner;

import com.pacific.adapter.SimpleItem;
import com.pacific.adapter.ViewHolder;

public final class Photo extends SimpleItem {

    private final String photoUrl;
    private final Object extra;

    public Photo(String photoUrl, Object extra) {
        this.photoUrl = photoUrl;
        this.extra = extra;
    }

    public String photoUrl() {
        return photoUrl;
    }

    public Object extra() {
        return extra;
    }

    @Override
    public int getLayout() {
        return R.layout.banner_photo;
    }

    @Override
    public void bind(ViewHolder holder) {
        holder.attachOnClickListener(R.id.image);
        holder.attachImageLoader(R.id.image);
    }
}
