package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.OfferAdapterBinding;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {
    private Context context;
    private List<String> stringList;

    public OfferAdapter(Context context,List<String> stringList)
    {
        this.context=context;
        this.stringList=stringList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       OfferAdapterBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.offer_adapter,parent,false);
        return new OfferAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvDis.setText(""+stringList.get(position));


    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        OfferAdapterBinding  binding;

        public MyViewHolder(OfferAdapterBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
