package com.pacific.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pacific.adapter.AdapterUtil;
import com.pacific.adapter.ImageLoader;
import com.pacific.adapter.PagerAdapter2;
import com.rd.PageIndicatorView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.pacific.adapter.AdapterUtil.findView;

public class BannerView extends ConstraintLayout implements View.OnTouchListener {

    private TextView text;
    private ImageView icon;
    private ViewPager pager;
    private View mask;
    private PagerAdapter2 adapter;
    private PageIndicatorView indicator;

    private boolean showNewsIcon;
    private int newsMaskColor;
    private String newsText;
    private Drawable newsIcon;
    private boolean attached = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setOnPhotoClickListener(OnClickListener listener) {
        adapter.setOnClickListener(listener);
    }

    public void setImageLoader(ImageLoader imageLoader) {
        adapter.setImageLoader(imageLoader);
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    //when user press Home Key or start another Activity it will be triggered
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (attached) {
            if (visibility == View.VISIBLE) {
                play();
            } else {
                stop();
            }
        }
    }

    //useful in ListView , GridView or RecyclerView
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attached = true;
        play();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attached = false;
        stop();
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = context.getResources().getColor(R.color.mask, null);
        } else {
            color = context.getResources().getColor(R.color.mask);
        }
        showNewsIcon = typedArray.getBoolean(R.styleable.BannerView_show_news_icon, false);
        newsMaskColor = typedArray.getColor(R.styleable.BannerView_news_mask_color, color);
        newsText = typedArray.getString(R.styleable.BannerView_news_text);
        newsIcon = typedArray.getDrawable(R.styleable.BannerView_news_icon);
        typedArray.recycle();
        LayoutInflater.from(context).inflate(R.layout.banner, this);
        text = findView(this, R.id.text);
        icon = findView(this, R.id.img);
        pager = findView(this, R.id.pager);
        mask = findView(this, R.id.mask);
        indicator = findView(this, R.id.indicator);
        if (showNewsIcon) {
            icon.setVisibility(VISIBLE);
        } else {
            icon.setVisibility(INVISIBLE);
        }
        mask.setBackgroundColor(newsMaskColor);
        if (!TextUtils.isEmpty(newsText)) {
            text.setText(newsText);
        }
        if (newsIcon != null) {
            icon.setImageDrawable(newsIcon);
        }
        pager.setOnTouchListener(this);
        adapter = new PagerAdapter2();
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //stop playing when user scroll ViewPager
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_MOVE:
                stop();
                break;
            case MotionEvent.ACTION_CANCEL:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                play();
                break;
        }
        return false;
    }

    private void stop() {
        compositeDisposable.clear();
    }

    private void play() {
        if (compositeDisposable.size() > 0 || adapter.getCount() <= 1) return;
        Disposable disposable = Observable.interval(5, 5, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        int position = adapter.getCurrentPosition();
                        if (position < adapter.getCount() - 1) return position + 1;
                        return 0;
                    }
                })
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            pager.setCurrentItem(integer, false);
                        } else {
                            pager.setCurrentItem(integer);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void addAll(List<Photo> list) {
        adapter.addAll(AdapterUtil.toItems(list));
        indicator.setViewPager(pager);
    }

    public void remove(Photo photo) {
        adapter.remove(photo);
        indicator.setViewPager(pager);
    }

    public void add(Photo photo) {
        adapter.add(photo);
        indicator.setViewPager(pager);
    }

    public void replace(int index, Photo photo) {
        adapter.replaceAt(index, photo);
        indicator.setViewPager(pager);
    }

    public void replaceAll(List<Photo> list) {
        adapter.replaceAll(AdapterUtil.toItems(list));
        indicator.setViewPager(pager);
    }

    public void setNewsText(@StringRes int resId) {
        text.setText(resId);
    }

    public void setNewsText(String str) {
        text.setText(str);
    }

    public void setIconImageResource(@DrawableRes int resId) {
        icon.setImageResource(resId);
    }

    public void setIconImageResource(@NonNull Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setMaskColor(int color) {
        mask.setBackgroundColor(color);
    }

    public void setMaskBackgroundResource(@DrawableRes int resId) {
        mask.setBackgroundResource(resId);
    }

    @TargetApi(16)
    public void setMaskBackground(Drawable drawable) {
        mask.setBackground(drawable);
    }
}
