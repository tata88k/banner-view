package com.pacific.demo;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pacific.adapter.DefaultBinding;
import com.pacific.adapter.SimpleRecyclerItem;
import com.pacific.adapter.ViewHolder;

public class Content extends SimpleRecyclerItem {
    private String imageUrl;
    private String name;
    private String description;

    public Content(String imageUrl, String name, String description) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
    }

    @Override
    public int getLayout() {
        return R.layout.item_content;
    }

    @Override
    public void bind(ViewHolder holder) {
        DefaultBinding binding = holder.binding();
        binding.setText(R.id.text_name, name);
        binding.setText(R.id.text_description, description);
        ImageView iv = binding.findView(R.id.img_icon);
        Glide.with((Activity) binding.context()).load(imageUrl).fitCenter().into(iv);
    }
}
