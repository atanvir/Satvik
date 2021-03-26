 package com.satvick.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemFilterBrandBinding;
import com.satvick.model.BrandCheckedListModel;

import java.util.ArrayList;
import java.util.List;

public class FilterProductListByBrandAdapter extends RecyclerView.Adapter<FilterProductListByBrandAdapter.FilterViewHolder> {
    Context context;
    LayoutInflater inflater;

    List<String> brandList;
    public ArrayList<String> checkList=new ArrayList<>();

    List<BrandCheckedListModel> brandNameCheckedList;


    public FilterProductListByBrandAdapter(Context context, List<BrandCheckedListModel> brandNameCheckedList) {
        this.context = context;
        this.brandNameCheckedList=brandNameCheckedList;

    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemFilterBrandBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_filter_brand, viewGroup, false);
        return new FilterProductListByBrandAdapter.FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterViewHolder filterViewHolder, final int position) {

        filterViewHolder.binding.filterBrandText.setText(brandNameCheckedList.get(position).getBrandName());

        if(brandNameCheckedList.get(position).isBrandChecked()){
            filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.VISIBLE);
            brandNameCheckedList.get(position).setBrandChecked(true);
            filterViewHolder.binding.filterBrandText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
            filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
        }else {
            filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);

            brandNameCheckedList.get(position).setBrandChecked(false);
            filterViewHolder.binding.filterBrandText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
            filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
        }

    }

    @Override
    public int getItemCount() {
        return brandNameCheckedList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        ItemFilterBrandBinding binding;

        public FilterViewHolder(ItemFilterBrandBinding itemFilterBrandBinding) {
            super(itemFilterBrandBinding.getRoot());
            this.binding = itemFilterBrandBinding;

            binding.mainLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!brandNameCheckedList.get(getAdapterPosition()).isBrandChecked()) {
                        binding.checkboxUncheckTick.setVisibility(View.VISIBLE);

                        brandNameCheckedList.get(getAdapterPosition()).setBrandChecked(true);
                        binding.filterBrandText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
                        notifyDataSetChanged();

                    }else {
                        binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);
                        brandNameCheckedList.get(getAdapterPosition()).setBrandChecked(false);
                        binding.filterBrandText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
                        notifyDataSetChanged();


                    }
                }
            });

        }
    }

}
