package com.satvick.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.satvick.R;
import com.satvick.databinding.AdapterSizeSubChartBinding;

import java.util.List;

public class SubSizeChartAdapter extends RecyclerView.Adapter<SubSizeChartAdapter.MyViewHolder> {

    private Context context;
    private List<String> footerList;

    public SubSizeChartAdapter(Context context, List<String> footerList)
    {
        this.context=context;
        this.footerList=footerList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2==0) return 0;
        else return 1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSizeSubChartBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_size_sub_chart,parent,false);
        return new SubSizeChartAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder.getItemViewType()==0)
        {
            holder.binding.subText.setBackgroundColor(ContextCompat.getColor(context,R.color.grey));
        }

        holder.binding.subText.setText(footerList.get(position));


    }

    @Override
    public int getItemCount() {
        return footerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterSizeSubChartBinding binding;

        public MyViewHolder(@NonNull AdapterSizeSubChartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;







        }
    }
}
