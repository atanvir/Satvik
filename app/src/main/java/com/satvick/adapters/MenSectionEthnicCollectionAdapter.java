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
import com.satvick.activities.ProductDetailsActivityFinal;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemWomenSectionEthnicCollectionBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenSectionEthnicCollectionAdapter extends RecyclerView.Adapter<MenSectionEthnicCollectionAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    private String symbol;
    private Double convertedPrice;

    List<InnerPagesModel.Data> otherData1;

    String filterData="";
    String type="";

    public MenSectionEthnicCollectionAdapter(Context context,List<InnerPagesModel.Data> otherData1, String filterData, String type) {
        this.context = context;
        this.otherData1=otherData1;
        this.filterData=filterData;
        this.type=type;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public MenSectionEthnicCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionEthnicCollectionBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_ethnic_collection, viewGroup, false);
        return new MenSectionEthnicCollectionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenSectionEthnicCollectionAdapter.ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = otherData1.get(position).getImage();
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

        viewHolder.binding.tvName.setText(otherData1.get(position).getBrand());
        viewHolder.binding.tvDesc.setText(otherData1.get(position).getName());
        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(otherData1.get(position).getSp())*convertedPrice));

        if(otherData1.get(position).getPercentage().equalsIgnoreCase("0")){
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        }else {
            viewHolder.binding.tvOff.setText(otherData1.get(position).getPercentage()+"% OFF");
            viewHolder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(otherData1.get(position).getMrp())*convertedPrice));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.binding.rlWmnSecEthnicCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailsActivityFinal.class);
                intent.putExtra(GlobalVariables.product_id,otherData1.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherData1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemWomenSectionEthnicCollectionBinding binding;

        ViewHolder(ItemWomenSectionEthnicCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
