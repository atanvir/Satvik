package com.satvick.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.CatgoryItemLayoutBinding;
import com.satvick.model.HomeSlidingProductResponse;

import java.util.List;


public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;

    List<HomeSlidingProductResponse> categoryList;

    public CategoryItemAdapter(Context context, List<HomeSlidingProductResponse> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       /* LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.catgory_item_layout, viewGroup, false);
        return new ViewHolder(view);*/

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        CatgoryItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.catgory_item_layout, viewGroup, false);
        return new CategoryItemAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.tvItemName.setText(categoryList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      /*  TextView tvItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.categoryItemRecyclerView);
        }*/

        CatgoryItemLayoutBinding binding;

        ViewHolder(CatgoryItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
