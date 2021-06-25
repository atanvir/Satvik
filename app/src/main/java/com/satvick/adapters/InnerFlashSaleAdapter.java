package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.ProductCategoriesActivity;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.InnerFlashSaleBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class InnerFlashSaleAdapter extends RecyclerView.Adapter<InnerFlashSaleAdapter.ViewHolder> {

    private Context context;
    private List<InnerPagesModel.Flash_sale> list;

    public InnerFlashSaleAdapter(Context context, List<InnerPagesModel.Flash_sale> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public InnerFlashSaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InnerFlashSaleAdapter.ViewHolder(InnerFlashSaleBinding.inflate(LayoutInflater.from(context), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerFlashSaleAdapter.ViewHolder viewHolder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(viewHolder.binding.image);
        viewHolder.binding.tvPrice.setText("₹ "+Math.round(Double.parseDouble(list.get(position).getSp())));
        if(list.get(position).getPercentage().equalsIgnoreCase("0")) { viewHolder.binding.tvCuttedText.setVisibility(View.GONE);}
        else {
            viewHolder.binding.tvCuttedText.setText("₹ "+Math.round(Double.parseDouble(list.get(position).getMrp())));
            viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() { return list!=null?list.size():0; }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        InnerFlashSaleBinding binding;
        ViewHolder(InnerFlashSaleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.llFlashSale.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llFlashSale :
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra(GlobalVariables.product_id,list.get(getAbsoluteAdapterPosition()).getProduct_id());
                context.startActivity(intent);
                break;
            }
        }
    }
}
