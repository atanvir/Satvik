package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.databinding.AdapterProductByCategoriesBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;


import java.util.List;

public class ProductByCategoryAdapter extends RecyclerView.Adapter<ProductByCategoryAdapter.ViewHolder> {

    private Context context;
    private List<InnerPagesModel.Data> list;


    public ProductByCategoryAdapter(Context context, List<InnerPagesModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductByCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductByCategoryAdapter.ViewHolder(AdapterProductByCategoriesBinding.inflate(LayoutInflater.from(context), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductByCategoryAdapter.ViewHolder viewHolder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(viewHolder.binding.image);
        viewHolder.binding.tvDesc.setText(list.get(position).getName());
        viewHolder.binding.tvPrice.setText("₹ "+list.get(position).getSp());

        if(list.get(position).getPercentage().equalsIgnoreCase("0")){
            viewHolder.binding.tvCuttedText.setVisibility(View.GONE);
            viewHolder.binding.tvOff.setVisibility(View.GONE);
        }else {
            viewHolder.binding.tvOff.setText(list.get(position).getPercentage()+"% OFF");
            viewHolder.binding.tvCuttedText.setText("₹ "+Math.round(Double.parseDouble(list.get(position).getMrp())));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterProductByCategoriesBinding binding;

        public ViewHolder(@NonNull AdapterProductByCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.rlGymTees.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context, ProductDetailActivity.class);
            intent.putExtra(GlobalVariables.product_id,list.get(getAbsoluteAdapterPosition()).getProduct_id());
            context.startActivity(intent);
        }
    }
}
