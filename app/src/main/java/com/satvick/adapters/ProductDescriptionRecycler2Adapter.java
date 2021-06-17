package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.R;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemProductDetailRecyclerBinding;
import com.satvick.model.CustomerAlsoLiked;

import java.util.List;

public class ProductDescriptionRecycler2Adapter extends RecyclerView.Adapter<ProductDescriptionRecycler2Adapter.ViewHolder> {

    private Context context;
    LayoutInflater inflater;
    private Double convertedPrice;
    private String symbol;
    private List<CustomerAlsoLiked> alsoLikedslist;


    public ProductDescriptionRecycler2Adapter(Context context, List<CustomerAlsoLiked> alsoLikedslist) {
        this.context = context;
        this.alsoLikedslist=alsoLikedslist;
        setHasStableIds(true);
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return alsoLikedslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemProductDetailRecyclerBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_product_detail_recycler, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(alsoLikedslist.get(position).getImage()).listener(new RequestListener<Drawable>() {
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
        }).into(holder.binding.imgPeoductDetailRecycler);
        holder.binding.tvDesc.setText(""+alsoLikedslist.get(position).getName());
        holder.binding.textPeoductDetailRecycler.setText(""+alsoLikedslist.get(position).getBrand());
        holder.binding.spTxt.setText(symbol+" "+Math.round(Double.parseDouble(alsoLikedslist.get(position).getSp())*convertedPrice));
        holder.binding.mrpTxt.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.mrpTxt.setText(symbol+" "+Math.round(Double.parseDouble(alsoLikedslist.get(position).getMrp())*convertedPrice));
        if(alsoLikedslist.get(position).getDiscount()==0)
        {
            holder.binding.offTxt.setVisibility(View.GONE);
        }
        holder.binding.offTxt.setText(""+alsoLikedslist.get(position).getDiscount()+"% OFF");
        holder.binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sendId", String.valueOf(alsoLikedslist.get(position).getProductId()));
                context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra("product_id",String.valueOf(alsoLikedslist.get(position).getProductId())));

            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductDetailRecyclerBinding binding;

        ViewHolder(ItemProductDetailRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
