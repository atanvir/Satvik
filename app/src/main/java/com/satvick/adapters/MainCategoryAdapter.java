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
import com.satvick.model.HomeModel;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<HomeModel.Man> list;


    public MainCategoryAdapter(Context context, List<HomeModel.Man> list) {
        this.context = context;
        this.list = list;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MainCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliding_home2, parent, false);
        return new MainCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        holder.tvProductName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvProductName;

        public ViewHolder(View binding) {
            super(binding);
            image = binding.findViewById(R.id.image);
            tvProductName = binding.findViewById(R.id.tvProductName);
            binding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callingIntent("HomeFragmentAfterLogin", "" + list.get(getAdapterPosition()).getId(), "", list.get(getAdapterPosition()).getSlug());

                }
            });
        }


        private void callingIntent(String from, String subcatid, String filterType, String category_name) {
            Intent intent = new Intent(context, MenActivity.class);
            intent.putExtra("from", "HomeFragmentAfterLoginMen");
            intent.putExtra("filterData", category_name);
            intent.putExtra("type", "Category");
            context.startActivity(intent);


        }

    }
}

