package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.activities.ProductCategoriesActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.AdapterFlashSaleBinding;
import com.satvick.databinding.AdapterInnerBannerBinding;
import com.satvick.databinding.AdapterInnerProductsBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;


import java.util.List;

public class InnerProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<InnerPagesModel.Other_section> list;
    private final int LAYOUT_FLASH_SALE=1, LAYOUT_PRODUCT=2, LAYOUT_BANNER=3;

    public InnerProductAdapter(Context context, List<InnerPagesModel.Other_section> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==LAYOUT_FLASH_SALE) return new FlashSaleViewHolder(AdapterFlashSaleBinding.inflate(LayoutInflater.from(context), viewGroup, false));
        else if(viewType==LAYOUT_BANNER) return new BannerViewHolder(AdapterInnerBannerBinding.inflate(LayoutInflater.from(context), viewGroup, false));
        else return new ProductViewHolder(AdapterInnerProductsBinding.inflate(LayoutInflater.from(context), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if(viewHolder instanceof FlashSaleViewHolder){
            ((FlashSaleViewHolder) viewHolder).binding.rvFlashSale.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            ((FlashSaleViewHolder) viewHolder).binding.rvFlashSale.setAdapter(new InnerFlashSaleAdapter(context,list.get(position).getFlash_sale()));
        }else if(viewHolder instanceof  BannerViewHolder){

            ((BannerViewHolder) viewHolder).binding.viewPager.setAdapter(new InnerBannerAdapter(context,list.get(position).getBannerList()));

        }else if(viewHolder instanceof ProductViewHolder){
            ((ProductViewHolder) viewHolder).binding.tvHeader.setText(list.get(position).getSectionName());
            ((ProductViewHolder) viewHolder).binding.rvProducts.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            ((ProductViewHolder) viewHolder).binding.rvProducts.setAdapter(new ProductByCategoryAdapter(context,list.get(position).getData()));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFlash_sale()!=null) return LAYOUT_FLASH_SALE;
        else if(list.get(position).getBannerList()!=null) return LAYOUT_BANNER;
        else return LAYOUT_PRODUCT;
    }


    public class FlashSaleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterFlashSaleBinding binding;
        public FlashSaleViewHolder(@NonNull  AdapterFlashSaleBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.tvViewAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvViewAll:
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from",GlobalVariables.flashSale);
                intent.putExtra(GlobalVariables.section_name,"Flash Sale");
                context.startActivity(intent);
                break;

            }
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private AdapterInnerBannerBinding binding;
        public BannerViewHolder(@NonNull  AdapterInnerBannerBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

    }


    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterInnerProductsBinding binding;
        public ProductViewHolder(@NonNull AdapterInnerProductsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.binding.tvViewAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tvViewAll :
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from","MenActivity");
                intent.putExtra(GlobalVariables.subcatid,""+list.get(getAbsoluteAdapterPosition()).getSubcatId());
                intent.putExtra(GlobalVariables.section_name,list.get(getAbsoluteAdapterPosition()).getSectionName());
                context.startActivity(intent);
                break;
            }
        }
    }

}
