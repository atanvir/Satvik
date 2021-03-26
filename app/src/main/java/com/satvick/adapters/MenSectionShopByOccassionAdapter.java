package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

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
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemWomenSectionShopByOccasionBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MenSectionShopByOccassionAdapter extends RecyclerView.Adapter<MenSectionShopByOccassionAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<InnerPagesModel.Occasion_list> occasionLists;
    String filterData="";
    String type="";

    private int[] image = {
            R.drawable.white_blezer,
            R.drawable.blue_waist_coat,
            R.drawable.white_blezer,
    };

    public MenSectionShopByOccassionAdapter(Context context,List<InnerPagesModel.Occasion_list> occasionLists, String filterData, String type) {
        this.context = context;
        this.occasionLists=occasionLists;
        this.filterData=filterData;
        this.type=type;
    }

    @NonNull
    @Override
    public MenSectionShopByOccassionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionShopByOccasionBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_shop_by_occasion, viewGroup, false);
        return new MenSectionShopByOccassionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenSectionShopByOccassionAdapter.ViewHolder viewHolder, final int position) {


        String imgUrlStyleFeed = occasionLists.get(position).getImage();
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

        viewHolder.binding.tvName.setText(occasionLists.get(position).getName());


        viewHolder.binding.rlWmnSecShopByOcsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from","MenSectionShopByOccassionAdapter");
                intent.putExtra(GlobalVariables.subsubcatid,String.valueOf(occasionLists.get(position).getFilter_data()));
                intent.putExtra(GlobalVariables.section_name,occasionLists.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return occasionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ItemWomenSectionShopByOccasionBinding binding;

        ViewHolder(ItemWomenSectionShopByOccasionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
