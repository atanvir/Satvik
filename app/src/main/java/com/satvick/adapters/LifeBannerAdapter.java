package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.satvick.databinding.AdapterLifeBannerBinding;
import com.satvick.model.BannerBean;

import java.util.List;

public class LifeBannerAdapter extends RecyclerView.Adapter<LifeBannerAdapter.MyViewHolder> {
    private Context context;
    private List<BannerBean> list;

    public LifeBannerAdapter(Context context,List<BannerBean> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public LifeBannerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new LifeBannerAdapter.MyViewHolder(AdapterLifeBannerBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LifeBannerAdapter.MyViewHolder holder, int position) {
        holder.binding.tvWriteName.setText(list.get(position).getWritter());
        holder.binding.tvDesc.setText(list.get(position).getDesc());
        Glide.with(context).load("https://soulahe.com/public/banner/bg.jpeg").into(holder.binding.ivCoverPhoto);
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterLifeBannerBinding binding;

        public MyViewHolder(@NonNull AdapterLifeBannerBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
