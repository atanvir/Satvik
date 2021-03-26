package com.satvick.activities;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.satvick.R;
import com.satvick.databinding.ItemReferralsListBinding;
import com.satvick.model.ReferalListModel;

import java.util.List;

public class MyReferralsListAdapter extends RecyclerView.Adapter<MyReferralsListAdapter.ViewHolder> {
    Context context;
    List<ReferalListModel.List> referalListArrayList;


    public MyReferralsListAdapter(Context context,List<ReferalListModel.List> referalListArrayList) {
        this.context = context;
        this.referalListArrayList = referalListArrayList;

    }


    @Override
    public MyReferralsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemReferralsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_referrals_list, viewGroup, false);
        return new MyReferralsListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyReferralsListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.binding.tvName.setText(referalListArrayList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return referalListArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemReferralsListBinding binding;

        ViewHolder(ItemReferralsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
