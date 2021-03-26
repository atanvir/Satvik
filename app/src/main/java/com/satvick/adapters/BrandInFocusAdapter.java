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
import com.satvick.databinding.BrandsInFocusLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class BrandInFocusAdapter extends RecyclerView.Adapter<BrandInFocusAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;

    List<HomeModel.Brandinfocu> brandinfocusList;


    public BrandInFocusAdapter(Context context,List<HomeModel.Brandinfocu> brandinfocusList) {
        this.context = context;
        this.brandinfocusList=brandinfocusList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        BrandsInFocusLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.brands_in_focus_layout, viewGroup, false);
        return new BrandInFocusAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = brandinfocusList.get(position).getImage();
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

        viewHolder.binding.rlBrandInFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProductListActivity.class).putExtra("from","BrandInFocusAdapter")
                        .putExtra(GlobalVariables.section_name,brandinfocusList.get(position).getFilterData())
                        .putExtra("type",brandinfocusList.get(position).getType()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandinfocusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        BrandsInFocusLayoutBinding binding;

        ViewHolder(BrandsInFocusLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
