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
import com.satvick.databinding.ItemWomenSectionDressesAndJumpsuitsBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenSectionDressesAndJumpsuitsAdapter extends RecyclerView.Adapter<MenSectionDressesAndJumpsuitsAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    private Double convertedPrice;
    List<InnerPagesModel.Data> otherData2;
    private String symbol;

    String filterData="";
    String type="";

    public MenSectionDressesAndJumpsuitsAdapter(Context context,List<InnerPagesModel.Data> otherData2,String filterData,String type) {
        this.context = context;
        this.otherData2=otherData2;
        this.filterData=filterData;
        this.type=type;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");

    }

    @NonNull
    @Override
    public MenSectionDressesAndJumpsuitsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionDressesAndJumpsuitsBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_dresses_and_jumpsuits, viewGroup, false);
        return new MenSectionDressesAndJumpsuitsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenSectionDressesAndJumpsuitsAdapter.ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = otherData2.get(position).getImage();
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

        viewHolder.binding.tvName.setText(otherData2.get(position).getBrand());

        viewHolder.binding.tvDesc.setText(otherData2.get(position).getName());
        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(""+otherData2.get(position).getSp())*convertedPrice));

        if(otherData2.get(position).getPercentage().equalsIgnoreCase("0")){
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        }else {
            viewHolder.binding.tvOff.setText(otherData2.get(position).getPercentage()+"% OFF");
            viewHolder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(otherData2.get(position).getMrp())*convertedPrice));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.binding.rlWmnSecDressesAndJumpsuits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra(GlobalVariables.product_id,otherData2.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherData2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ItemWomenSectionDressesAndJumpsuitsBinding binding;

        ViewHolder(ItemWomenSectionDressesAndJumpsuitsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
