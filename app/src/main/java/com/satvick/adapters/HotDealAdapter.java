package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
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
import com.satvick.databinding.HotDealLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class HotDealAdapter extends RecyclerView.Adapter<HotDealAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;

    List<HomeModel.Hotdeal> hotDealList;

    public HotDealAdapter(Context context, List<HomeModel.Hotdeal> hotDealList ) {
        this.context = context;
        this.hotDealList = hotDealList;
    }

    @NonNull
    @Override
    public HotDealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        HotDealLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.hot_deal_layout, viewGroup, false);
        return new HotDealAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final HotDealAdapter.ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = hotDealList.get(position).getImage();
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

        viewHolder.binding.rlHotDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProductListActivity.class)
                        .putExtra("from","HotDealAdapter")
                        .putExtra(GlobalVariables.subcatid, hotDealList.get(position).getFilterData())
                        .putExtra(GlobalVariables.section_name,"Hotdeal"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotDealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        HotDealLayoutBinding binding;

        ViewHolder(HotDealLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
