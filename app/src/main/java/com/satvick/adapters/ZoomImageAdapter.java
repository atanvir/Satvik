package com.satvick.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.databinding.AdapterZoomImageBinding;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ZoomImageAdapter extends PagerAdapter {
    private Context context;
    private List<String> list;

    public ZoomImageAdapter(Context context, List<String> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        AdapterZoomImageBinding binding=AdapterZoomImageBinding.inflate(LayoutInflater.from(context),container,false);
        Glide.with(context).load(list.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@androidx.annotation.Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                binding.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(binding.imageView);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }


    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view==object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
