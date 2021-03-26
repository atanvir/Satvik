package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemBannerMenBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenBannerAdapter extends RecyclerView.Adapter<MenBannerAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<InnerPagesModel.Subcategory_slider> subcategory_sliderList;
    String filterData="";
    String type="";


    public MenBannerAdapter(Context context, List<InnerPagesModel.Subcategory_slider> subcategory_sliderList,String filterData,String type) {
        this.context = context;
        this.subcategory_sliderList=subcategory_sliderList;
        this.filterData=filterData;
        this.type=type;
    }

    @NonNull
    @Override
    public MenBannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemBannerMenBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_banner_men, viewGroup, false);
        return new MenBannerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenBannerAdapter.ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = subcategory_sliderList.get(position).getImage();
        if (imgUrlStyleFeed != null && imgUrlStyleFeed.length() > 0) {
            Glide.with(context).load(imgUrlStyleFeed).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    viewHolder.binding.progressbar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.binding.image);
        }

        viewHolder.binding.tvName.setText(subcategory_sliderList.get(position).getName());


        viewHolder.binding.llSliderBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from","MenBannerAdapter");
                intent.putExtra(GlobalVariables.subcatid,String.valueOf(subcategory_sliderList.get(position).getFilter_data()));
                intent.putExtra(GlobalVariables.section_name,subcategory_sliderList.get(position).getName());
                intent.putExtra("type",type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategory_sliderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemBannerMenBinding binding;

        ViewHolder(ItemBannerMenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
