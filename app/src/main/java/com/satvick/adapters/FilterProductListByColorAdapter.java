package com.satvick.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemFilterColorBinding;
import com.satvick.model.ColorCheckedListModel;

import java.util.ArrayList;
import java.util.List;

public class FilterProductListByColorAdapter extends RecyclerView.Adapter<FilterProductListByColorAdapter.FilterViewHolder> {
    Context context;
    LayoutInflater inflater;

    public ArrayList<String> checkList=new ArrayList<>();

    /////////////////by making Model, using boolean, checked,unchecked checkbox////////////////
    List<ColorCheckedListModel> colorNameCheckedList;


//    public FilterProductListByColorAdapter(Context context, List<String> colorList) {
//        this.context = context;
//        this.colorList=colorList;
//        setHasStableIds(true);
//    }

    public FilterProductListByColorAdapter(Context context, List<ColorCheckedListModel> colorNameCheckedList) {
        this.context = context;
        this.colorNameCheckedList=colorNameCheckedList;

    }


    @NonNull
    @Override
    public FilterProductListByColorAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemFilterColorBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_filter_color, viewGroup, false);

        return new FilterProductListByColorAdapter.FilterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final FilterProductListByColorAdapter.FilterViewHolder filterViewHolder, final int position) {
        if(colorNameCheckedList.get(position).getHexCode().contains("##"))
        {
            String hexCode=colorNameCheckedList.get(position).getHexCode().replace("##", "#");
            colorNameCheckedList.get(position).setHexCode(hexCode);
        }
        int color=Color.parseColor(colorNameCheckedList.get(position).getHexCode());
        filterViewHolder.binding.filterColor.setBackgroundColor(color);

        filterViewHolder.binding.filterColorText.setText(colorNameCheckedList.get(position).getColorName());

        if(colorNameCheckedList.get(position).isColorChecked()){
            filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.VISIBLE);

            colorNameCheckedList.get(position).setColorChecked(true);
            filterViewHolder.binding.filterColorText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
            filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
        }else {
            filterViewHolder.binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);

            colorNameCheckedList.get(position).setColorChecked(false);
            filterViewHolder.binding.filterColorText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
            filterViewHolder.binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
        }

    }

    @Override
    public int getItemCount() {
        //return colorList.size();
        return colorNameCheckedList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        ItemFilterColorBinding binding;

        public FilterViewHolder(ItemFilterColorBinding itemFilterColorBinding) {
            super(itemFilterColorBinding.getRoot());
            this.binding = itemFilterColorBinding;

            binding.mainLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!colorNameCheckedList.get(getAdapterPosition()).isColorChecked()) {
                        binding.checkboxUncheckTick.setVisibility(View.VISIBLE);
                        colorNameCheckedList.get(getAdapterPosition()).setColorChecked(true);
                        binding.filterColorText.setTextColor(context.getResources().getColor(R.color.colorLogOut));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable.tick_copy_new);
                        notifyDataSetChanged();
                    }else {
                        binding.checkboxUncheckTick.setVisibility(View.INVISIBLE);
                        colorNameCheckedList.get(getAdapterPosition()).setColorChecked(false);
                        binding.filterColorText.setTextColor(context.getResources().getColor(R.color.colorLiteGrey));
                        binding.checkboxUncheckTick.setButtonDrawable(R.drawable._tick_grel_icon);
                        notifyDataSetChanged();
                    }
                }
            });

        }
    }

}