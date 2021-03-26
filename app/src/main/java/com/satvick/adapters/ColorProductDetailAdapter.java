package com.satvick.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemColorBinding;

import java.util.List;


public class ColorProductDetailAdapter extends RecyclerView.Adapter<ColorProductDetailAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;

    ColorsChangedListener listener;

    List<String> color;
    String colorName="";
    boolean defaultColorSet=false;
    int j=-1;//position declaration

    public ColorProductDetailAdapter(Context context, List<String> color) {
        this.context = context;
        this.color = color;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemColorBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_color, viewGroup, false);
        return new ColorProductDetailAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        if(!color.get(position).equalsIgnoreCase("")) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.drawable_black_circle);
            int colors = Color.parseColor(color.get(position));
            drawable.setColorFilter(new PorterDuffColorFilter(colors, PorterDuff.Mode.SRC_ATOP));
            viewHolder.binding.ivYellowColor.setBackground(drawable);


            if (!defaultColorSet) {
                    defaultColorSet = true;
                    j = position;

            }

            if (j == position) {
                //pink
                viewHolder.binding.containerLL.setBackground(ContextCompat.getDrawable(context, R.drawable.drawable_circle_red));
            } else {
                //black
                viewHolder.binding.containerLL.setBackground(ContextCompat.getDrawable(context, R.drawable.drawable_black_circle3));
            }
        }else
        {
            viewHolder.binding.containerLL.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return color.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemColorBinding binding;

        ViewHolder(ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.containerLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    j=getAdapterPosition();
                    notifyDataSetChanged();
                    if(listener!=null){
                        listener.itemClick(view,colorName,j);
                    }
                }
            });

        }
    }

   public interface ColorsChangedListener{
        void itemClick(View view,String colorName,int pos);
    }

    public void setListener(ColorsChangedListener listener) {
        this.listener = listener;
    }
}
