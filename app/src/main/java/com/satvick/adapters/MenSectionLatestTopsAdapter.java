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
import com.satvick.databinding.ItemWomenSectionLatestTopsBinding;

public class MenSectionLatestTopsAdapter extends RecyclerView.Adapter<MenSectionLatestTopsAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;


    private int[] image = {
            R.drawable.zym_t_shirts,
            R.drawable.black_t_shirts,
            R.drawable.zym_t_shirts,
    };

    public MenSectionLatestTopsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MenSectionLatestTopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemWomenSectionLatestTopsBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_women_section_latest_tops, viewGroup, false);
        return new MenSectionLatestTopsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenSectionLatestTopsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.binding.image.setImageResource(image[i]);

        viewHolder.binding.tvCuttedText.setPaintFlags(viewHolder.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.binding.rlWmnSecLatestTops.setOnClickListener(new View.OnClickListener() {
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


        ItemWomenSectionLatestTopsBinding binding;

        ViewHolder(ItemWomenSectionLatestTopsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
