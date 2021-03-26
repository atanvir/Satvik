package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

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
import com.satvick.activities.ProductDetailsActivityFinal;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemMenSectionGymTeesListBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenSectionGymClothsAdapter extends RecyclerView.Adapter<MenSectionGymClothsAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    private Double convertedPrice;
    private String symbol;

    List<InnerPagesModel.Data> otherData3;

    String filterData="";
    String type="";

    public MenSectionGymClothsAdapter(Context context, List<InnerPagesModel.Data> otherData3, String filterData, String type) {
        this.context = context;
        this.otherData3=otherData3;
        this.filterData=filterData;
        this.type=type;
        this.convertedPrice = Double.valueOf(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString("converted_amount")));
        this.symbol = SharedPreferenceWriter.getInstance(context).getString("symbol");
    }

    @NonNull
    @Override
    public MenSectionGymClothsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemMenSectionGymTeesListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_men_section_gym_tees_list, viewGroup, false);
        return new MenSectionGymClothsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenSectionGymClothsAdapter.ViewHolder viewHolder, final int position) {

        String imgUrlStyleFeed = otherData3.get(position).getImage();
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

        viewHolder.binding.tvName.setText(otherData3.get(position).getBrand());

        viewHolder.binding.tvDesc.setText(otherData3.get(position).getName());
        viewHolder.binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(otherData3.get(position).getSp())*convertedPrice));

        if(otherData3.get(position).getPercentage().equalsIgnoreCase("0")){
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        }else {
            viewHolder.binding.tvOff.setText(otherData3.get(position).getPercentage()+"% OFF");
            viewHolder.binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(otherData3.get(position).getMrp())*convertedPrice));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.binding.rlGymTees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailsActivityFinal.class);
                intent.putExtra(GlobalVariables.product_id,otherData3.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return otherData3.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMenSectionGymTeesListBinding binding;

        ViewHolder(ItemMenSectionGymTeesListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
