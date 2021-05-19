package com.satvick.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.MenActivity;
import com.satvick.databinding.ItemSlidingHome2Binding;
import com.satvick.model.HomeModel;
import com.satvick.model.ProductBean;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    private Context context;
    private List<ProductBean> list;


    public MainCategoryAdapter(Context context, List<ProductBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public MainCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCategoryAdapter.ViewHolder(ItemSlidingHome2Binding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(MainCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.binding.image);
        holder.binding.tvProductName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemSlidingHome2Binding binding;

        public ViewHolder(ItemSlidingHome2Binding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.rlMain.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.rlMain :
                Intent intent = new Intent(context, MenActivity.class);
                intent.putExtra("from", "HomeFragmentAfterLoginMen");
                intent.putExtra("filterData", list.get(getAdapterPosition()).getSlug());
                intent.putExtra("type", "Category");
                context.startActivity(intent);
                break;

            }
        }
    }
}

