package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.ProductCategoriesActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.databinding.ItemBannerMenBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class InnerCategoriesAdapter extends RecyclerView.Adapter<InnerCategoriesAdapter.ViewHolder> {

    private Context context;
    private List<InnerPagesModel.Subcategory_slider> list;


    public InnerCategoriesAdapter(Context context, List<InnerPagesModel.Subcategory_slider> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public InnerCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new InnerCategoriesAdapter.ViewHolder(ItemBannerMenBinding.inflate(LayoutInflater.from(context),viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerCategoriesAdapter.ViewHolder viewHolder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(viewHolder.binding.image);
        viewHolder.binding.tvName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemBannerMenBinding binding;
        ViewHolder(ItemBannerMenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.llSliderBanner.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llSliderBanner:
                Intent intent=new Intent(context,ProductListActivity.class);
                intent.putExtra("from","MenBannerAdapter");
                intent.putExtra(GlobalVariables.subcatid,String.valueOf(list.get(getAbsoluteAdapterPosition()).getFilter_data()));
                intent.putExtra(GlobalVariables.section_name,list.get(getAbsoluteAdapterPosition()).getName());
                intent.putExtra("type",((ProductCategoriesActivity)context).getIntent().getStringExtra("type"));
                context.startActivity(intent);
                break;
            }
        }
    }
}
