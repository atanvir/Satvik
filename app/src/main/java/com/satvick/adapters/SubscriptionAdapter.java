package com.satvick.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.satvick.databinding.AdapterSubscriptionBinding;
import com.satvick.model.ArticleModel;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder> {
    private Context context;
    private List<ArticleModel.Data> list;

    public SubscriptionAdapter(Context context,List<ArticleModel.Data> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public SubscriptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscriptionAdapter.MyViewHolder(AdapterSubscriptionBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.MyViewHolder holder, int position) {
          holder.binding.tvOrder.setText(holder.binding.tvOrder.getText()+" "+list.get(position).getOrder_number());
        Glide.with(context).load(list.get(position).getImage()).into(holder.binding.ivProduct);
        holder.binding.tvName.setText(list.get(position).getTitle());
        holder.binding.tvDesc.setText(Html.fromHtml(list.get(position).getShort_desc()));
        holder.binding.tvPrice.setText("₹ "+list.get(position).getAmount());
        holder.binding.tvStartDate.setText(list.get(position).getStart_date());
        holder.binding.tvEndDate.setText(list.get(position).getEnd_date());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterSubscriptionBinding binding;

        public MyViewHolder(@NonNull AdapterSubscriptionBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
