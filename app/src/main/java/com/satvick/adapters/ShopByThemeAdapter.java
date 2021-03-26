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
import com.satvick.activities.ShopByThemeActivity;
import com.satvick.databinding.ShopByThemeLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class ShopByThemeAdapter extends RecyclerView.Adapter<ShopByThemeAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<HomeModel.Themedetail> themeDetailsList;

    private int[] image = {
            R.drawable.theam_cloths,
            R.drawable.kurta_img,
            R.drawable.theam_cloths,
            R.drawable.kurta_img,
            R.drawable.theam_cloths,
    };

    public ShopByThemeAdapter(Context context,List<HomeModel.Themedetail> themeDetailsList ) {
        this.context = context;
        this.themeDetailsList=themeDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       /* LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.shop_by_theme_layout,viewGroup,false);
        return new ViewHolder(view);*/

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ShopByThemeLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.shop_by_theme_layout, viewGroup, false);
        return new ShopByThemeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = themeDetailsList.get(position).getImage();
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

        viewHolder.binding.titleText.setText(themeDetailsList.get(position).getTitle());
        viewHolder.binding.descText.setText(themeDetailsList.get(position).getDescription());


        viewHolder.binding.llShortByTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, ProductListActivity.class).putExtra("from","adapter"));
                Intent intent=new Intent(context,ShopByThemeActivity.class);
                intent.putExtra("himimage", themeDetailsList.get(position).getHimimage());
                intent.putExtra("herimage", themeDetailsList.get(position).getHerimage());
                intent.putExtra("description", themeDetailsList.get(position).getDescription());
                intent.putExtra(GlobalVariables.subcatid, themeDetailsList.get(position).getFilterData());
                intent.putExtra(GlobalVariables.section_name, themeDetailsList.get(position).getTitle());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return themeDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      /*  public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }*/

        ShopByThemeLayoutBinding binding;

        ViewHolder(ShopByThemeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
