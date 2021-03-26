package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.AdapterTrackOrderBinding;
import com.satvick.model.MyOrderDetailsModel;

import java.util.List;

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.MyViewHolder> {

    private Context context;
    private List<MyOrderDetailsModel.Notifystatus> list;
    public TrackOrderAdapter(Context context, List<MyOrderDetailsModel.Notifystatus> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       AdapterTrackOrderBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_track_order,parent,false);
       return new TrackOrderAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvDate.setText(list.get(position).getDate());
        holder.binding.tvTitle.setText(list.get(position).getContent());
        if(list.size()-1==position) {
            holder.binding.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterTrackOrderBinding binding;

        public MyViewHolder(AdapterTrackOrderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;



        }
    }
}
