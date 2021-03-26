package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.AdapterShopByThemeBinding;
import com.satvick.model.ShopByThemeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class ShopByTheme extends RecyclerView.Adapter<ShopByTheme.MyViewHolder> {
    private Context context;
    private List<ShopByThemeModel> imgList;

    public ShopByTheme(Context context,List<ShopByThemeModel> imgList)
    {
        this.context=context;
        this.imgList=imgList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterShopByThemeBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adapter_shop_by_theme,parent,false);
        return new ShopByTheme.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Glide.with(context).load(imgList.get(position).getImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.binding.progressbar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.binding.progressbar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.binding.themeImage);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopByThemeBinding binding;

        public MyViewHolder(@NonNull AdapterShopByThemeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

            binding.themeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String theme="";
                    if(getAdapterPosition()==0) {
                        theme="MEN";
                    }
                    else if(getAdapterPosition()==1) {
                        theme="WOMEN";
                    }


                    context.startActivity(new Intent(context,ProductListActivity.class)
                            .putExtra("from","ShopByThemeAdapter")
                            .putExtra(GlobalVariables.subcatid,""+imgList.get(getAdapterPosition()).getSubcatId())
                            .putExtra(GlobalVariables.section_name,imgList.get(getAdapterPosition()).getSection_name())
                            .putExtra(GlobalVariables.theme,theme)); }
            });
        }
    }
}
