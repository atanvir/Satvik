package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.AdapterProductDescriptionBinding;
import com.satvick.model.ProductDetailsResponse;

import java.util.List;

public class ProductDescAdapter extends RecyclerView.Adapter<ProductDescAdapter.MyViewHolder> {
    private Context context;
    private List<ProductDetailsResponse.Attribute> allAttributesList;


    public ProductDescAdapter(Context context,List<ProductDetailsResponse.Attribute> allAttributesList)
    {
        this.context=context;
        this.allAttributesList=allAttributesList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      AdapterProductDescriptionBinding view= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adapter_product_description,parent,false);
      return new ProductDescAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.headingTv.setText(allAttributesList.get(position).getKeyName());
        holder.binding.descTv.setText(allAttributesList.get(position).getKeyValue());
    }

    @Override
    public int getItemCount() {
        return allAttributesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterProductDescriptionBinding binding;

        public MyViewHolder(AdapterProductDescriptionBinding view) {
            super(view.getRoot());
            binding=view;

        }
    }
}
