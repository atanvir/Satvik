package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemWomenSectionFootWearBinding;

public class MenSectionFootWearAdapter extends RecyclerView.Adapter<MenSectionFootWearAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;


    private int[] image = {
            R.drawable.gym_accessries,
            R.drawable.hand_golf_men,
            R.drawable.gym_accessries,
    };

    public MenSectionFootWearAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MenSectionFootWearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionFootWearBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_foot_wear, viewGroup, false);
        return new MenSectionFootWearAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenSectionFootWearAdapter.ViewHolder viewHolder, int i) {
        viewHolder.binding.image.setImageResource(image[i]);

        viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        viewHolder.binding.rlWmnSecFootWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ProductListActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ItemWomenSectionFootWearBinding binding;

        ViewHolder(ItemWomenSectionFootWearBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
