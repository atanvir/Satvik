package com.satvick.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemSelectSizeBinding;

import java.util.List;



public class SelectSizeProductDetailsAdapter extends RecyclerView.Adapter<SelectSizeProductDetailsAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;

    List<String> size;

    SelectSizeListener listener;

    int j=-1;

    public SelectSizeProductDetailsAdapter(Context context, List<String> size) {
        this.context = context;
        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        ItemSelectSizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_select_size, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        viewHolder.binding.tvSize.setText(size.get(position));

        if(j==position) {
            viewHolder.binding.ivCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.drawable_solid_circle_pink));
            viewHolder.binding.tvSize.setTextColor(Color.parseColor("#ffffff"));

        }else {
            viewHolder.binding.ivCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.drawable_black_circle2));
            viewHolder.binding.tvSize.setTextColor(Color.parseColor("#000000"));
        }

        viewHolder.binding.tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                j=position;
                SelectSizeProductDetailsAdapter.this.notifyDataSetChanged();

                if(listener!=null){
                    listener.itemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return size.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSelectSizeBinding binding;

        ViewHolder(ItemSelectSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface SelectSizeListener{
        void itemClick(View view,int pos);
    }

    public void setListener(SelectSizeListener listener) {
        this.listener = listener;
    }
}
