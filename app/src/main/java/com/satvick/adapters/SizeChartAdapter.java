package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.AdapterSizeChartBinding;
import com.satvick.model.Sizechart;

import java.util.List;

public class SizeChartAdapter extends RecyclerView.Adapter<SizeChartAdapter.MyViewHolder> {

    private Context context;
    private List<Sizechart> sizechartList;

    public  SizeChartAdapter(Context context,List<Sizechart> sizechartList)
    {
        this.context=context;
        this.sizechartList=sizechartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSizeChartBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_size_chart,parent,false);
        return new SizeChartAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvHeading.setText(sizechartList.get(position).getKey());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        holder.binding.recyclerView.setLayoutManager(manager);
        holder.binding.recyclerView.setAdapter(new SubSizeChartAdapter(context,sizechartList.get(position).getValue()));



    }

    @Override
    public int getItemCount() {
        return sizechartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterSizeChartBinding binding;

        public MyViewHolder(@NonNull AdapterSizeChartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;







        }
    }
}
