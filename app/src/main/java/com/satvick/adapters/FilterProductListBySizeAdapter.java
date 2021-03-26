package com.satvick.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemFilterSizeBinding;
import com.satvick.model.SizeCheckedListModel;

import java.util.ArrayList;
import java.util.List;

public class FilterProductListBySizeAdapter extends RecyclerView.Adapter<FilterProductListBySizeAdapter.FilterViewHolder> {
    Context context;
    LayoutInflater inflater;

    List<String> sizeList;
    //Create an ArrayList object named checkList,so that we could add checked item in that object.
    public ArrayList<String> checkList=new ArrayList<>();

    //by making Model, using boolean, checked,unchecked checkbox
    List<SizeCheckedListModel> sizeCheckedList;


//    public FilterProductListBySizeAdapter(Context context, List<String> sizeList) {
//        this.context = context;
//        this.sizeList=sizeList;
//        setHasStableIds(true);
//    }


    public FilterProductListBySizeAdapter(Context context, List<SizeCheckedListModel> sizeCheckedList) {
        this.context = context;
        this.sizeCheckedList=sizeCheckedList;

    }


    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemFilterSizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_filter_size, viewGroup, false);

        return new FilterProductListBySizeAdapter.FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterViewHolder filterViewHolder, final int position) {

            filterViewHolder.binding.filterSizeText.setText(sizeCheckedList.get(position).getSizeName());

            if (sizeCheckedList.get(position).isSizeChecked()) {
                filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.VISIBLE);
                sizeCheckedList.get(position).setSizeChecked(true);
                filterViewHolder.binding.filterSizeText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
                filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
            } else {
                filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);
                sizeCheckedList.get(position).setSizeChecked(false);
                filterViewHolder.binding.filterSizeText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
                filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
            }


    }


    @Override
    public int getItemCount() {
       return sizeCheckedList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        ItemFilterSizeBinding binding;

        public FilterViewHolder(ItemFilterSizeBinding itemFiterSizeBinding) {
            super(itemFiterSizeBinding.getRoot());
            this.binding = itemFiterSizeBinding;

            binding.mainLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!sizeCheckedList.get(getAdapterPosition()).isSizeChecked()) {
                        binding.checkboxUncheckTick.setVisibility(View.VISIBLE);
                        sizeCheckedList.get(getAdapterPosition()).setSizeChecked(true);
                        binding.filterSizeText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
                        notifyDataSetChanged();

                    }else {
                        binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);
                        sizeCheckedList.get(getAdapterPosition()).setSizeChecked(false);
                        binding.filterSizeText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
                        notifyDataSetChanged();

                    }

                }
            });

        }
    }

}
