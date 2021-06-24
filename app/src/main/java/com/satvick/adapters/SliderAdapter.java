package com.satvick.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ZoomImageActivity;
import com.satvick.utils.FullImagePopUpWithZoom;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    //private List<Integer> color;
    private int[] image;

    List<String> color;

    SparseArray<View> views = new SparseArray<View>();



    public SliderAdapter(Context context, List<String> color) {
        this.context = context;
        this.color = color;
    }

    @Override
    public int getCount() {
        Log.e("count", String.valueOf(color.size()));
        return color.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_slider, container,false);

        setView(view,position);


        final ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        views.put(position, view);


        return view;
    }

    public void setData(List<String> color)
    {

        this.color=color;
        notifyDataSetChanged();
    }

    private void setView(View view, final int position) {

        ImageView linearLayout = view.findViewById(R.id.image_view_slider);
        final LottieAnimationView animationView=view.findViewById(R.id.progressbar);

        String imgUrl = color.get(position);
        if (imgUrl != null && imgUrl.length() > 0) {
            Glide.with(context).load(imgUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    animationView.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    animationView.setVisibility(View.GONE);
                    return false;
                }
            }).into(linearLayout);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                new FullImagePopUpWithZoom(context,imgUrl).show();
                context.startActivity(new Intent(context, ZoomImageActivity.class).putStringArrayListExtra("images",new ArrayList<String>(color)).putExtra("position",position));
            }
        });



    }


    @Override
    public void notifyDataSetChanged()
    {
        try
        {

            int key = 0;
            for(int i = 0; i < views.size(); i++) {
                key = views.keyAt(i);
                View view = views.get(key);
                setView(view,i);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
        views.remove(position);

    }

}