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
import com.satvick.activities.MenActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.AdapterCategoriesBinding;
import com.satvick.model.CategoriesBeans;
import com.satvick.model.CategoryModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    public Context context;
    public List<CategoriesBeans> list;

    public CategoriesAdapter(Context context, List<CategoriesBeans> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesAdapter.MyViewHolder(AdapterCategoriesBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.MyViewHolder holder, int position) {
        holder.binding.tvCategoryName.setText(list.get(position).category);
        if(!list.get(position).viewAll) holder.binding.tvViewAll.setVisibility(View.VISIBLE);
        else holder.binding.tvViewAll.setVisibility(View.GONE);
        holder.binding.rvProducts.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        holder.binding.rvProducts.setAdapter(new ProductAdapter(context,list.get(position).getCategory_slug(),list.get(position).getProducts()));
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterCategoriesBinding binding;
        public MyViewHolder(@NonNull AdapterCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
            binding.tvViewAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvViewAll :
                if(list.get(getAdapterPosition()).getCategory().equalsIgnoreCase("Product of the week")){
                    Intent intent=new Intent(context,ProductListActivity.class);
                    intent.putExtra("from","HomeFragmentAfterLogin");
                    intent.putExtra(GlobalVariables.subcatid,"");
                    intent.putExtra(GlobalVariables.subcatname,list.get(getAdapterPosition()).getCategory_slug());
                    context.startActivity(intent);

                }else {
                    Intent intent = new Intent(context, MenActivity.class);
                    intent.putExtra("from", "HomeFragmentAfterLogin");
                    intent.putExtra("filterData", list.get(getAdapterPosition()).getCategory_slug());
                    intent.putExtra("type", list.get(getAdapterPosition()).getCategory_slug());
                    context.startActivity(intent);
                }
                break;
            }
        }
    }
}
