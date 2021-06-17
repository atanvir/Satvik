package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
import com.satvick.databinding.ItemWomenSectionFlashSaleBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenSectionFlashSaleAdapter extends RecyclerView.Adapter<MenSectionFlashSaleAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<InnerPagesModel.Flash_sale> flashSaleList;
    String filterData="";
    String type="";
    private Double convertedPrice;
    private String symbol;


    private int[] image = {
            R.drawable.nike_t_shirts_men,
            R.drawable.blue_shirts_ss,
            R.drawable.nike_t_shirts_men,
    };

    public MenSectionFlashSaleAdapter(Context context, List<InnerPagesModel.Flash_sale> flashSaleList, String filterData, String type) {
        this.context = context;
        this.flashSaleList=flashSaleList;
        this.filterData=filterData;
        this.type=type;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public MenSectionFlashSaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionFlashSaleBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_flash_sale, viewGroup, false);
        return new MenSectionFlashSaleAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenSectionFlashSaleAdapter.ViewHolder viewHolder, final int position) {
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

        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(flashSaleList.get(position).getSp())*convertedPrice));

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

                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra(GlobalVariables.product_id,flashSaleList.get(position).getProduct_id());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return flashSaleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ItemWomenSectionFlashSaleBinding binding;

        ViewHolder(ItemWomenSectionFlashSaleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
