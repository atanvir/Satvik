package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.AdapterBankOfferBinding;

import java.util.List;

public class BankOfferAdapter extends RecyclerView.Adapter<BankOfferAdapter.MyViewHolder> {
    private Context context;
    private List<String> headerList;

    private List<String> stringList;

    public BankOfferAdapter(Context context, List<String> stringList, List<String> headerList) {
        this.context = context;
        this.stringList = stringList;
        this.headerList = headerList;
    }

    public int getItemCount() {
        return this.stringList.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt) {
        AdapterBankOfferBinding binding= DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.adapter_bank_offer, paramViewGroup, false);
        return new BankOfferAdapter.MyViewHolder(binding);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        holder.binding.tvOfferLabel.setText(headerList.get(pos)+":");
        holder.binding.tvDis.setText(stringList.get(pos));
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterBankOfferBinding binding;

        public MyViewHolder(AdapterBankOfferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
