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
import com.satvick.databinding.ShopCollectionLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class ShopCollectionAdapter extends RecyclerView.Adapter<ShopCollectionAdapter.ViewHolder> {
    private Context context;
    LayoutInflater inflater;

    List<HomeModel.Slider2> slider2List;

    public ShopCollectionAdapter(Context context, List<HomeModel.Slider2> slider2List) {
        this.context = context;
        this.slider2List=slider2List;
    }

    @NonNull
    @Override
    public ShopCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ShopCollectionLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.shop_collection_layout, viewGroup, false);
        return new ShopCollectionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopCollectionAdapter.ViewHolder viewHolder, final int position) {


        String imgUrlStyleFeed = slider2List.get(position).getImage();
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

        viewHolder.binding.rlGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProductListActivity.class)
                        .putExtra("from","ShopCollectionAdapter")
                        .putExtra(GlobalVariables.subcatid, slider2List.get(position).getFilterData())
                        .putExtra(GlobalVariables.section_name,"Shop By Collection"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return slider2List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShopCollectionLayoutBinding binding;

        ViewHolder(ShopCollectionLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}