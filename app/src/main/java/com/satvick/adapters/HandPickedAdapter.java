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
import com.satvick.databinding.HandpickedLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class HandPickedAdapter extends RecyclerView.Adapter<HandPickedAdapter.ViewHolder>
{
    private Context context;
    LayoutInflater inflater;

    List<HomeModel.Handpicked> handpickedList;
    private int[] image = {
            R.drawable.handipicked,
            R.drawable.casual_shirts,
            R.drawable.hand_band,
            R.drawable.yellow_dress,

    };

    public HandPickedAdapter(Context context, List<HomeModel.Handpicked> handpickedList) {
        this.context = context;
        this.handpickedList=handpickedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        HandpickedLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.handpicked_layout, viewGroup, false);
        return new HandPickedAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = handpickedList.get(position).getImage();
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
                        .putExtra("from","HandPickedAdapter")
                        .putExtra(GlobalVariables.subcatid, handpickedList.get(position).getFilterData())
                        .putExtra(GlobalVariables.section_name,"HANDPICKED"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return handpickedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

       /* public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }*/
       HandpickedLayoutBinding binding;

        ViewHolder(HandpickedLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
