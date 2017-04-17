package com.pacific.demo;

import android.view.View;
import android.widget.Toast;

import com.pacific.adapter.DefaultBinding;
import com.pacific.adapter.ImageLoader;
import com.pacific.adapter.SimpleRecyclerItem;
import com.pacific.adapter.ViewHolder;
import com.pacific.banner.BannerView;
import com.pacific.banner.Photo;

import java.util.List;

public class Header extends SimpleRecyclerItem {
    private List<Photo> photos;
    private String tip;
    private ImageLoader loader;

    public Header(List<Photo> photos, String tip, ImageLoader loader) {
        this.photos = photos;
        this.tip = tip;
        this.loader = loader;
    }

    @Override
    public int getLayout() {
        return R.layout.item_header;
    }

    @Override
    public void bind(ViewHolder viewHolder) {
        DefaultBinding binding = viewHolder.binding();
        BannerView bannerView = binding.findView(R.id.banner);
        bannerView.replaceAll(photos);
        bannerView.setNewsText(tip);
        if (loader != null) {
            bannerView.setImageLoader(loader);
        }
        bannerView.setOnPhotoClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), tip, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
