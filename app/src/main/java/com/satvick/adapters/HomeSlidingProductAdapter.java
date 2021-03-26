package com.satvick.adapters;

import android.content.Context;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.satvick.R;
import com.satvick.activities.ProductListActivity;
import com.satvick.model.HomeModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class HomeSlidingProductAdapter extends RecyclerView.Adapter<HomeSlidingProductAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<HomeModel.Woman> list;

public HomeSlidingProductAdapter(Context context, List<HomeModel.Woman> list) {
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
    public HomeSlidingProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliding_home, parent, false);
        return new HomeSlidingProductAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeSlidingProductAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        holder.tvProductName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvProductName;

        public ViewHolder(View binding) {
            super(binding);
            image=binding.findViewById(R.id.image);
            tvProductName=binding.findViewById(R.id.tvProductName);
            binding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callingIntent("HomeFragmentAfterLogin",""+ list.get(getAdapterPosition()).getId(),"",list.get(getAdapterPosition()).getCategoryId());

                }
            });

        }


        private void callingIntent(String from, String subcatid,String filterType,String category_name) {

            Intent intent=new Intent(context, ProductListActivity.class);
            intent.putExtra("from",from);
            intent.putExtra(GlobalVariables.subcatid,subcatid);
            intent.putExtra(GlobalVariables.subcatname,category_name);
            context.startActivity(intent);
        }


}


        private void callingIntent(String from, String subcatid,String filterType,String category_name) {
            Intent intent=new Intent(context, ProductListActivity.class);
            intent.putExtra("from",from);
            intent.putExtra(GlobalVariables.subcatid,subcatid);
            intent.putExtra(GlobalVariables.subcatname,category_name);
            context.startActivity(intent);
        }
}


