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
import com.satvick.databinding.GymLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder> {

    private Context context;
    LayoutInflater inflater;

    List<HomeModel.Slider1> slider1List;

    private int[] image = {
            R.drawable.new_cloths,
            R.drawable.men_vest,
            R.drawable.new_imgs,
            R.drawable.men_vest,

    };

    public GymAdapter(Context context, List<HomeModel.Slider1> slider1List) {
        this.context = context;
        this.slider1List=slider1List;
    }

    @NonNull
    @Override
    public GymAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        /*LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.handpicked_layout,viewGroup,false);
        return new ViewHolder(view);*/

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        GymLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.gym_layout, viewGroup, false);
        return new GymAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String imgUrlStyleFeed = slider1List.get(position).getImage();
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
                        .putExtra("from","GymAdapter")
                        .putExtra(GlobalVariables.subcatid, slider1List.get(position).getFilterData())
                        .putExtra(GlobalVariables.section_name,"GYM"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return slider1List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       /* public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }*/

        GymLayoutBinding binding;

        ViewHolder(GymLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
