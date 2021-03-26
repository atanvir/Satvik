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
import com.satvick.databinding.StyleFeedLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class StyleFeedAdapter extends RecyclerView.Adapter<StyleFeedAdapter.ViewHolder> {

    List<HomeModel.Stylefeed> stylefeedList;

    LayoutInflater inflater;
    Context context;

    private int[] image = {
            R.drawable.perfume,
            R.drawable.tshirt_img,
            R.drawable.dix,
            R.drawable.perfume,
    };

    public StyleFeedAdapter(Context context,List<HomeModel.Stylefeed> stylefeedList) {
        this.context = context;
        this.stylefeedList=stylefeedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        StyleFeedLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.style_feed_layout, viewGroup, false);
        return new StyleFeedAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = stylefeedList.get(position).getImage();
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

        viewHolder.binding.rlStyleFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProductListActivity.class)
                        .putExtra("from","StyleFeedAdapter")
                        .putExtra(GlobalVariables.subcatid, stylefeedList.get(position).getFilterData())
                        .putExtra(GlobalVariables.section_name,"Style Feed"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return stylefeedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /*public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }*/

        StyleFeedLayoutBinding binding;

        ViewHolder(StyleFeedLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
