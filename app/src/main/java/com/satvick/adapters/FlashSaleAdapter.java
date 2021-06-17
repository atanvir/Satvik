package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
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
import com.satvick.activities.ProductDetailActivity;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FlashSaleLayoutBinding;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;


public class FlashSaleAdapter extends RecyclerView.Adapter<FlashSaleAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;

    private Double convertedPrice;

    private String symbol;
    List<HomeModel.FlashSale> flashSaleList;


    public FlashSaleAdapter(Context context,List<HomeModel.FlashSale> flashSaleList) {
        this.context = context;
        this.flashSaleList=flashSaleList;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        FlashSaleLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.flash_sale_layout, viewGroup, false);
        return new FlashSaleAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = flashSaleList.get(position).getImage();
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

        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(""+flashSaleList.get(position).getSp())*convertedPrice));

        if(flashSaleList.get(position).getPercentage().equalsIgnoreCase("0"))
        {
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);

        }
        else
        {
            viewHolder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(flashSaleList.get(position).getMrp())*convertedPrice));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }



        viewHolder.binding.llFlashSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ProductDetailActivity.class)
                        .putExtra("from","FlashSaleAdapter")
                        .putExtra(GlobalVariables.product_id, flashSaleList.get(position).getProductId()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return flashSaleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FlashSaleLayoutBinding binding;

        ViewHolder(FlashSaleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
