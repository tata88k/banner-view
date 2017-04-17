# BannerView
A rxJava2 + ViewPager banner view for android. It automatically plays images and stops when on touching.

![](https://github.com/thepacific/banner-view/blob/master/banner-view/live.gif)

# Setup
```groovy
compile 'com.github.thepacific:banner:0.0.1'
```

# XML
```html
<com.pacific.banner.BannerView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="170dp"
        app:news_icon=""
        app:news_text=""
        app:news_mask_color=""
        app:show_news_icon="true|false" >
</com.pacific.banner.BannerView>

<declare-styleable name="BannerView">
        <attr name="show_news_icon" format="boolean" />
        <attr name="news_mask_color" format="reference" />
        <attr name="news_icon" format="reference" />
        <attr name="news_text" format="string" />
</declare-styleable>
```

# Java
```java
        bannerView.addAll(List< Photo > list);

        bannerView.remove(Photo photo);

        bannerView.add(Photo photo);

        bannerView.replace(int index, Photo photo);

        bannerView.replaceAll(List<Photo> list);

        bannerView.setNewsText(@StringRes int resId);

        bannerView.setNewsText(String str);

        bannerView.setIconImageResource(@DrawableRes int resId);

        bannerView.setIconImageResource(@NonNull Drawable drawable);

        bannerView.setMaskColor(int color);

        bannerView.setMaskBackgroundResource(@DrawableRes int resId);

        bannerView.setMaskBackground(Drawable drawable);

```

# License  
[The MIT License ](https://opensource.org/licenses/MIT)
