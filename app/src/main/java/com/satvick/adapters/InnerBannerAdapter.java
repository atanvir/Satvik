package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.activities.ProductCategoriesActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemSliderBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class InnerBannerAdapter extends PagerAdapter {
    private Context context;
    private List<InnerPagesModel.Bannerlist> list;

    public InnerBannerAdapter(Context context, List<InnerPagesModel.Bannerlist> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ItemSliderBinding binding=ItemSliderBinding.inflate(LayoutInflater.from(context),container,false);
        binding.progressbar.setVisibility(View.VISIBLE);
        Glide.with(context).load(list.get(position).getImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable  GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                binding.progressbar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.progressbar.setVisibility(View.GONE);
                return false;
            }
        }).into(binding.imageViewSlider);
        binding.imageViewSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductListActivity.class);
                intent.putExtra("from","MenActivity");
                intent.putExtra(GlobalVariables.subcatid,list.get(position).getFilter_data());
                intent.putExtra(GlobalVariables.section_name,list.get(position).getName());
                context.startActivity(intent);
            }
        });

        container.addView(binding.getRoot(), 0);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View) object);
    }
}
