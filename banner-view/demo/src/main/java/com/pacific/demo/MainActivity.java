package com.pacific.demo;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pacific.adapter.AdapterUtil;
import com.pacific.adapter.ImageLoader;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerItem;
import com.pacific.adapter.ViewHolder;
import com.pacific.banner.BannerView;
import com.pacific.banner.Photo;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new RecyclerAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        load();
    }

    private void load() {
        Observable.just("http://i.imgur.com/")
                .map(host -> {
                    List<RecyclerItem> items = new ArrayList<>();
                    String ext = ".jpg";
                    String[] headerUrls = {
                            host + "7GUv9qa" + ext, host + "i5vXmXp" + ext,
                            host + "HvTyeh3" + ext, host + "Ig9oHCM" + ext,
                            host + "DAl0KB8" + ext, host + "xZLIYFV" + ext
                    };

                    List<Photo> photos = new ArrayList<>();
                    for (int i = 0; i < headerUrls.length; i++) {
                        photos.add(new Photo(headerUrls[i], null));
                    }
                    items.add(new Header(
                            photos,
                            getString(R.string.tip),
                            (v, holder) -> {
                                Photo photo = holder.getItem();
                                Glide.with(this).load(photo.photoUrl()).centerCrop().into(v);
                            }));

                    String[] contentUrls = {
                            host + "CqmBjo5" + ext, host + "zkaAooq" + ext, host + "0gqnEaY" + ext,
                            host + "9gbQ7YR" + ext, host + "aFhEEby" + ext, host + "0E2tgV7" + ext,
                            host + "P5JLfjk" + ext, host + "nz67a4F" + ext, host + "dFH34N5" + ext,
                            host + "FI49ftb" + ext, host + "DvpvklR" + ext, host + "DNKnbG8" + ext,
                            host + "yAdbrLp" + ext, host + "55w5Km7" + ext, host + "NIwNTMR" + ext,
                            host + "DAl0KB8" + ext, host + "xZLIYFV" + ext, host + "HvTyeh3" + ext,
                            host + "Ig9oHCM" + ext, host + "7GUv9qa" + ext, host + "i5vXmXp" + ext,
                            host + "glyvuXg" + ext, host + "u6JF6JZ" + ext, host + "ExwR7ap" + ext,
                            host + "Q54zMKT" + ext, host + "9t6hLbm" + ext, host + "F8n3Ic6" + ext,
                            host + "P5ZRSvT" + ext, host + "jbemFzr" + ext, host + "8B7haIK" + ext,
                            host + "aSeTYQr" + ext, host + "OKvWoTh" + ext, host + "zD3gT4Z" + ext,
                            host + "z77CaIt" + ext
                    };
                    for (int i = 0; i < contentUrls.length; i++) {
                        items.add(new Content(contentUrls[i], "Todo_" + i, "use glide to load image " + i));
                    }

                    return items;
                })
                .subscribeOn(Schedulers.newThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> adapter.addAll(items));
    }
}